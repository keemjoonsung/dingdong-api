import {BusWrapper} from "@/pages/BusTracker/components/BusOverlay/styles.ts";
import {CustomOverlayMap} from "react-kakao-maps-sdk";
import {PositionType} from "@/pages/BusTracker/page.tsx";

export default function BusOverlay({lat, lng}: PositionType) {

    return (
        <CustomOverlayMap // 커스텀 오버레이를 표시할 Container
            // 커스텀 오버레이가 표시될 위치입니다
            position={{
                lat: lat,
                lng: lng,
            }}
        >
            <BusWrapper>
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 14 14" fill="none">
                    <path d="M4.52598 0.583344H9.47417C9.94372 0.583337 10.3313 0.583331 10.647 0.609124C10.9749 0.635914 11.2763 0.69341 11.5594 0.837663C11.9984 1.06137 12.3554 1.41832 12.5791 1.85737C12.6217 1.94097 12.6567 2.02617 12.6856 2.11318C12.7319 2.25245 12.755 2.32209 12.7343 2.40456C12.7181 2.46877 12.664 2.5437 12.6082 2.57929C12.5365 2.62501 12.4475 2.62501 12.2694 2.62501H1.73071C1.55267 2.62501 1.46365 2.62501 1.39193 2.57929C1.3361 2.54371 1.28204 2.46877 1.26587 2.40456C1.2451 2.32209 1.26824 2.25245 1.31452 2.11318C1.34343 2.02617 1.37846 1.94097 1.42106 1.85737C1.64476 1.41832 2.00172 1.06137 2.44076 0.837663C2.72388 0.69341 3.02529 0.635914 3.35318 0.609124C3.66887 0.583331 4.05643 0.583337 4.52598 0.583344Z" fill="white"/>
                    <path d="M1.63313 3.79168C1.46988 3.79168 1.38826 3.79168 1.32588 3.82346C1.27102 3.8514 1.22638 3.89603 1.19842 3.95088C1.16662 4.01326 1.1666 4.09485 1.16656 4.25803C1.16641 4.82203 1.16661 5.38602 1.1667 5.95001C1.16672 6.11339 1.16674 6.19507 1.19853 6.25745C1.2265 6.31233 1.27111 6.35693 1.32599 6.38489C1.38838 6.41668 1.47005 6.41668 1.63341 6.41668H12.3667C12.5301 6.41668 12.6118 6.41668 12.6742 6.38489C12.729 6.35693 12.7736 6.31233 12.8016 6.25745C12.8334 6.19507 12.8334 6.11339 12.8334 5.95001C12.8335 5.38602 12.8337 4.82203 12.8336 4.25804C12.8335 4.09485 12.8335 4.01326 12.8017 3.95088C12.7738 3.89603 12.7291 3.8514 12.6743 3.82346C12.6119 3.79168 12.5303 3.79168 12.367 3.79168H1.63313Z" fill="white"/>
                    <path fillRule="evenodd" clipRule="evenodd" d="M12.8334 8.05001C12.8334 7.88666 12.8334 7.80499 12.8016 7.7426C12.7737 7.68772 12.729 7.6431 12.6742 7.61513C12.6118 7.58334 12.5301 7.58334 12.3667 7.58334H1.63341C1.47006 7.58334 1.38838 7.58334 1.32599 7.61513C1.27111 7.6431 1.22649 7.68772 1.19853 7.7426C1.16674 7.80499 1.16674 7.88666 1.16674 8.05001V8.30744C1.16673 8.77699 1.16673 9.16455 1.19252 9.48024C1.21931 9.80813 1.27681 10.1095 1.42106 10.3927C1.62624 10.7953 1.94351 11.129 2.33341 11.3541L2.3334 12.3802C2.33337 12.4497 2.33334 12.5326 2.33932 12.6057C2.34624 12.6905 2.364 12.8118 2.42877 12.9389C2.51266 13.1036 2.64652 13.2374 2.81116 13.3213C2.9383 13.3861 3.05963 13.4038 3.14434 13.4108C3.21742 13.4167 3.30038 13.4167 3.36986 13.4167H4.50524C4.57471 13.4167 4.65772 13.4167 4.73081 13.4108C4.81552 13.4038 4.93685 13.3861 5.06398 13.3213C5.22862 13.2374 5.36248 13.1036 5.44637 12.9389C5.51115 12.8118 5.52891 12.6905 5.53583 12.6057C5.5418 12.5326 5.54177 12.4497 5.54174 12.3802L5.54174 11.6667H8.45841L8.4584 12.3802C8.45838 12.4497 8.45834 12.5326 8.46432 12.6057C8.47124 12.6905 8.489 12.8118 8.55377 12.9389C8.63766 13.1036 8.77152 13.2374 8.93616 13.3213C9.0633 13.3861 9.18463 13.4038 9.26934 13.4108C9.34242 13.4167 9.42538 13.4167 9.49485 13.4167H10.6302C10.6997 13.4167 10.7827 13.4167 10.8558 13.4108C10.9405 13.4038 11.0618 13.3861 11.189 13.3213C11.3536 13.2374 11.4875 13.1036 11.5714 12.9389C11.6361 12.8118 11.6539 12.6905 11.6608 12.6057C11.6668 12.5327 11.6668 12.4497 11.6667 12.3802L11.6667 11.3541C12.0566 11.129 12.3739 10.7953 12.5791 10.3927C12.7233 10.1095 12.7808 9.80813 12.8076 9.48024C12.8334 9.16455 12.8334 8.77701 12.8334 8.30745V8.05001ZM3.20841 9.04168C3.20841 8.71951 3.46957 8.45834 3.79174 8.45834H4.66674C4.98891 8.45834 5.25007 8.71951 5.25007 9.04168C5.25007 9.36384 4.98891 9.62501 4.66674 9.62501H3.79174C3.46957 9.62501 3.20841 9.36384 3.20841 9.04168ZM9.33341 8.45834C9.01124 8.45834 8.75007 8.71951 8.75007 9.04168C8.75007 9.36384 9.01124 9.62501 9.33341 9.62501H10.2084C10.5306 9.62501 10.7917 9.36384 10.7917 9.04168C10.7917 8.71951 10.5306 8.45834 10.2084 8.45834H9.33341Z" fill="white"/>
                </svg>
            </BusWrapper>
        </CustomOverlayMap>
    )
}