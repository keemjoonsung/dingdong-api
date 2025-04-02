package com.ddbb.dingdong.application.usecase.bus;

import com.ddbb.dingdong.application.common.Params;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetPathPointBySSEUseCase {
    private final DataSource dataSource;
    private final int BATCH_SIZE = 5000; // 더 큰 배치 크기

    @Async
    public void execute(Param param) {
        try {
            streamCompressedPoints(param.getBusScheduleId(), param.getEmitter());
        } catch (Exception e) {
            if (e instanceof IOException && e.getMessage() != null &&
                    (e.getMessage().contains("Broken pipe") || e.getMessage().contains("Connection reset"))) {
                log.info("Client disconnected: {}", e.getMessage());
            } else {
                log.error("Error streaming data", e);
                param.getEmitter().completeWithError(e);
            }
        }
    }

    private void streamCompressedPoints(Long busScheduleId, ResponseBodyEmitter sseEmitter) throws SQLException, IOException {
        String sql = "SELECT pt.longitude, pt.latitude " +
                "FROM point pt " +
                "JOIN line l ON l.id = pt.line_id " +
                "JOIN path p ON p.id = l.path_id " +
                "JOIN bus_schedule bs ON bs.id = p.bus_schedule_id " +
                "WHERE bs.id = ? " +
                "ORDER BY l.sequence, pt.sequence";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql,
                     ResultSet.TYPE_FORWARD_ONLY,
                     ResultSet.CONCUR_READ_ONLY)) {

            stmt.setFetchSize(Integer.MIN_VALUE);
            stmt.setLong(1, busScheduleId);

            try (ResultSet rs = stmt.executeQuery()) {
                // 배치 데이터 준비
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream(BATCH_SIZE * 8); // 초기 크기 예상
                int count = 0;

                while (rs.next()) {
                    // 바이너리로 직접 쓰기 (8바이트: float 2개)
                    float lng = (float) rs.getDouble(1);
                    float lat = (float) rs.getDouble(2);

                    // float 값을 바이트로 변환 (네트워크 바이트 순서)
                    int lngBits = Float.floatToIntBits(lng);
                    int latBits = Float.floatToIntBits(lat);

                    byteStream.write((lngBits >> 24) & 0xFF);
                    byteStream.write((lngBits >> 16) & 0xFF);
                    byteStream.write((lngBits >> 8) & 0xFF);
                    byteStream.write(lngBits & 0xFF);

                    byteStream.write((latBits >> 24) & 0xFF);
                    byteStream.write((latBits >> 16) & 0xFF);
                    byteStream.write((latBits >> 8) & 0xFF);
                    byteStream.write(latBits & 0xFF);

                    count++;

                    if (count >= BATCH_SIZE) {
                        // 압축 후 전송
                        sendCompressedBatch(sseEmitter, byteStream.toByteArray(), count);
                        byteStream.reset();
                        count = 0;
                    }
                }

                if (count > 0) {
                    sendCompressedBatch(sseEmitter, byteStream.toByteArray(), count);
                }

                sseEmitter.complete();
            }
        }
    }

    private void sendCompressedBatch(ResponseBodyEmitter sseEmitter, byte[] rawData, int pointCount) throws IOException {
        // 가볍고 빠른 압축 적용 (BEST_SPEED 모드로 설정)
        byte[] compressedData = fastCompress(rawData);

        // 메타데이터와 함께 압축된 데이터 전송
        BatchData batchData = new BatchData(compressedData, pointCount);
        sseEmitter.send(batchData, MediaType.APPLICATION_JSON);
    }

    /**
     * 매우 빠른 압축 알고리즘 적용 (CPU 부하 최소화)
     */
    private byte[] fastCompress(byte[] data) throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);

        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater)) {
            dos.write(data);
        }

        return baos.toByteArray();
    }

    /**
     * 압축된 데이터 전송을 위한 클래스
     */
    @Getter
    private static class BatchData {
        private final byte[] compressedData;
        private final int pointCount;

        public BatchData(byte[] compressedData, int pointCount) {
            this.compressedData = compressedData;
            this.pointCount = pointCount;
        }
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public static class Param implements Params {
        private Long busScheduleId;
        private ResponseBodyEmitter emitter;
    }
}