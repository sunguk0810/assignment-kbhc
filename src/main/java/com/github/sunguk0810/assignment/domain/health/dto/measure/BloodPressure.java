package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;

import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 수축기(Systolic), 이완기(Diastolic) 및 평균 동맥압(MAP)을 포함하는 혈압 측정 데이터 클래스입니다.
 * <p>
 * 모든 혈압 수치는 값과 단위를 포함하는 {@link Quantity} 객체로 표현됩니다.
 * 일반적으로 혈압의 단위는 {@code "mmHg"}를 사용합니다.
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
public class BloodPressure extends HealthDetail {
    /**
     * 수축기 혈압 (Systolic Blood Pressure)
     * <p>심장이 수축하여 혈액을 내보낼 때 혈관에 가해지는 가장 높은 압력입니다.</p>
     */
    private Quantity systolic;

    /**
     * 이완기 혈압 (Diastolic Blood Pressure)
     * <p>심장이 이완되어 혈액을 채울 때 혈관에 남아있는 가장 낮은 압력입니다.</p>
     */
    private Quantity diastolic;

    @Override
    public void applyTo(HealthMeasureSummary summary) {
        summary.updateSummary(this);
    }
}
