package com.github.sunguk0810.assignment.domain.health.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * HealthMeasureEvent 클래스는 건강 측정 이벤트에 대한 정보를 담고 있는 데이터 모델입니다.
 * 이 클래스는 측정 로그 및 관련 기록을 식별하는 데 필요한 데이터를 포함합니다.
 * 주로 건강 측정 데이터 관리 및 전송에 활용됩니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "건강 측정 이벤트 DTO")
public class HealthMeasureEvent {
    /**
     * 측정 로그의 고유 식별자를 나타내는 변수입니다.
     * 데이터베이스에서 각 측정 로그를 식별하기 위해 사용됩니다.
     */
    @Schema(description = "측정 로그 ID")
    private Long measureLogId;

    /**
     * 측정 기록의 고유 키를 나타내는 변수입니다.
     * 외부 시스템 또는 데이터베이스와 연계하여 측정 기록을 식별하는 데 사용됩니다.
     */
    @Schema(description = "사용자 구분 키")
    private String recordKey;
}
