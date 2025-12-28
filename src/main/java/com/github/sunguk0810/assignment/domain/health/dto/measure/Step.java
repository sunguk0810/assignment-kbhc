package com.github.sunguk0810.assignment.domain.health.dto.measure;

import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Step extends HealthDetail {
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
}
