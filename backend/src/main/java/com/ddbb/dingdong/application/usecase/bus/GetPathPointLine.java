package com.ddbb.dingdong.application.usecase.bus;

import com.ddbb.dingdong.application.common.CaffeineService;
import com.ddbb.dingdong.application.common.Params;
import com.ddbb.dingdong.application.common.UseCase;
import com.ddbb.dingdong.domain.transportation.repository.PathQueryRepository;
import com.ddbb.dingdong.domain.transportation.repository.projection.PathPointProjection;
import com.ddbb.dingdong.application.common.RedisService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPathPointLine implements UseCase<GetPathPointLine.Param, GetPathPointLine.Response> {
    private final PathQueryRepository pathQueryRepository;
    private final RedisService redisService;
    private final CaffeineService caffeineService;
    private static final String CACHE_PREFIX = "paths::";
    private static final long CACHE_TTL = 10; // 캐시 TTL (10분)

    @Override
    @Transactional(readOnly = true)
    public Response execute(Param param) {
        String cacheKey = CACHE_PREFIX + param.busScheduleId;
//        List<Response.Point> localCache = (List<Response.Point>) caffeineService.getData(cacheKey);
//        if(localCache != null) {
//            return new Response(localCache);
//        }
//        List<Response.Point> cachedPoints = (List<Response.Point>) redisService.getData(cacheKey);
//
//        if (cachedPoints != null) {
//            caffeineService.saveDate(cacheKey, cachedPoints);
//            return new Response(cachedPoints); // Redis에서 가져온 데이터를 Response 객체로 변환
//        }
        List<PathPointProjection> projections = pathQueryRepository.findPathPointsByPathId(param.busScheduleId);

//        List<Response.Point> points = projections.stream()
//                .map(proj -> new Response.Point(proj.getLongitude(), proj.getLatitude()))
//                .toList();
//        redisService.saveDate(cacheKey, points);

        return new Response(projections);
    }

    @Getter
    @AllArgsConstructor
    public static class Param implements Params {
        private Long busScheduleId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response  {
        private List<PathPointProjection> points;

        @Getter
        @AllArgsConstructor
        public static class Point implements Serializable { // ✅ Serializable 추가
            double longitude;
            double latitude;
        }
    }
}
