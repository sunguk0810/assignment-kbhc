package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

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
@Builder
public class OxygenSaturation {
    /**
     * 실제 측정된 산소포화도 데이터
     * <p>
     * 일반적인 단위: {@code "%"}
     * </p>
     */
    private Quantity oxygenSaturation;

    @Override
    public String toString() {
        return new StringJoiner(", ", OxygenSaturation.class.getSimpleName() + "[", "]")
                .add("oxygenSaturation=" + oxygenSaturation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OxygenSaturation that = (OxygenSaturation) o;
        return Objects.equals(oxygenSaturation, that.oxygenSaturation);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(oxygenSaturation);
    }
}
