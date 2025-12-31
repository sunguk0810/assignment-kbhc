package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 혈당(Blood Sugar) 측정 데이터를 나타내는 클래스입니다.
 * <p>
 * {@link HealthDetail} 인터페이스의 구현체 중 하나로,
 * 혈당 수치를 값과 단위를 포함하는 {@link Quantity} 객체로 관리합니다.
 * </p>
 *
 * @see Quantity
 * @see HealthDetail
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BloodSugar extends HealthDetail{

    /**
     * 실제 측정된 혈당 수치 데이터
     * <p>
     * 일반적인 단위: {@code "mg/dL"} 또는 {@code "mmol/L"}
     * </p>
     */
    private Quantity bloodSugar;


    @Override
    public void applyTo(HealthMeasureSummary summary) {
        summary.updateSummary(this);
    }
}
