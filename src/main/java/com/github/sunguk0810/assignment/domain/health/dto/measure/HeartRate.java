package com.github.sunguk0810.assignment.domain.health.dto.measure;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 심박수(Heart Rate) 측정 데이터를 나타내는 클래스입니다.
 * <p>
 * {@link HealthDetail} 인터페이스의 구현체 중 하나이며,
 * 심박수 수치를 값과 단위를 포함하는 {@link Quantity} 객체로 관리합니다.
 * </p>
 *
 * @see Quantity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HeartRate {
    /**
     * 실제 측정된 심박수 데이터
     * <p>
     * 일반적인 단위: {@code "bpm"} (beats per minute)
     * </p>
     */
    private Quantity heartRate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HeartRate heartRate1 = (HeartRate) o;
        return Objects.equals(heartRate, heartRate1.heartRate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(heartRate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HeartRate.class.getSimpleName() + "[", "]")
                .add("heartRate=" + heartRate)
                .toString();
    }
}
