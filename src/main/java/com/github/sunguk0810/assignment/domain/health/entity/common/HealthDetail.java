package com.github.sunguk0810.assignment.domain.health.entity.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.sunguk0810.assignment.domain.health.dto.measure.*;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 다양한 건강 측정 데이터(걸음 수, 혈압 등)를 추상화한 <b>최상위 추상 클래스</b>입니다.

 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class HealthDetail {
    /**
     * 측정 기간을 나타내는 공통 변수
     * 모든 하위 클래스(Step, BloodPressure 등)가 이 필드를 공유합니다.
     */
    private Period period;

    /**
     * 현재 측정 데이터(DTO)를 요약 엔티티(Entity)에 반영하는 추상 메서드입니다.
     * <p>
     * 각 하위 클래스는 이 메서드를 구현하여, {@link HealthMeasureSummary}의
     * 자신에게 맞는 {@code updateSummary} 메서드를 호출해야 합니다.
     * </p>
     *
     * <pre>
     * // 구현 예시 (Step 클래스)
     * &#64;Override
     * public void applyTo(HealthMeasureSummary summary) {
     * summary.updateSummary(this); // this는 Step 타입
     * }
     * </pre>
     *
     * @param summary 업데이트할 건강 요약 엔티티
     */
    public abstract void applyTo(HealthMeasureSummary summary);
}
