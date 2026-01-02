package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 산소포화도(Oxygen Saturation) 측정 데이터를 나타내는 클래스입니다.
 * <p>
 * {@link HealthDetail} 인터페이스의 구현체 중 하나이며,
 * 혈액 내 산소 농도 수치를 값과 단위를 포함하는 {@link Quantity} 객체로 관리합니다.
 * </p>
 *
 * @see Quantity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "산소포화도")
public class OxygenSaturation extends HealthDetail{
    /**
     * 실제 측정된 산소포화도 데이터
     * <p>
     * 일반적인 단위: {@code "%"}
     * </p>
     */
    @Schema(description = "산소포화도 수치")
    private Quantity oxygenSaturation;

    @Override
    public void applyTo(HealthMeasureSummary summary) {
        summary.updateSummary(this);
    }
}
