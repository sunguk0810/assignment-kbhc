package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 걸음 수(Steps) 및 관련 활동량 데이터를 나타내는 클래스입니다.
 * <p>
 * 단순 걸음 횟수뿐만 아니라, 해당 활동을 통해 이동한 거리와 소모된 칼로리 정보를
 * {@link Quantity} 객체를 통해 함께 관리합니다.
 * </p>
 *
 * @see Quantity
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Step {
    /**
     * 측정된 총 걸음 횟수
     * <p>단위: {@code count}</p>
     */
    private int steps;

    /**
     * 이동 거리 데이터
     * <p>일반적인 단위: {@code "m"} (미터) 또는 {@code "km"} (킬로미터)</p>
     */
    private Quantity distance;

    /**
     * 소모 칼로리 데이터
     * <p>일반적인 단위: {@code "kcal"}</p>
     */
    private Quantity calories;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return steps == step.steps && Objects.equals(distance, step.distance) && Objects.equals(calories, step.calories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, distance, calories);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Step.class.getSimpleName() + "[", "]")
                .add("steps=" + steps)
                .add("distance=" + distance)
                .add("calories=" + calories)
                .toString();
    }
}
