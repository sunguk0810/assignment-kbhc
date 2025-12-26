package com.github.sunguk0810.assignment.domain.health.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.dto.device.DeviceInfo;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * 건강 측정 데이터 저장을 위한 요청 DTO 클래스입니다.
 * <p>
 * 사용자 측정 데이터, 메타데이터, 장치 정보 등을 포함하며,
 * Jackson 라이브러리를 통해 JSON으로 직렬화/역직렬화됩니다.
 * </p>
 *
 * @see MeasureType
 */
@Data
public class MeasureSaveRequest {
    /**
     * 해당 측정 데이터의 고유 식별 키
     */
    private String recordKey;

    /**
     * 측정 데이터의 종류 (예: 혈압, 걸음 수 등)
     * @see MeasureType
     */
    private MeasureType type;

    /**
     * 실제 측정 데이터값, 메모, 장치 정보를 포함하는 상세 데이터 객체
     */
    private MeasureSaveRequest.Data data;

    /**
     * 데이터 최종 수정 일시
     * <p>포맷: {@code "yyyy-MM-dd HH:mm:ss Z"} (ISO 8601)</p>
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private ZonedDateTime lastUpdate;

    @lombok.Data
    public static class Data {
        private List<? extends HealthDetail> entries;
        private DeviceInfo source;
        private String memo;
    }
}
