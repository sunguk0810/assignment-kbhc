package com.github.sunguk0810.assignment.domain.health.entity.common;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 측정 데이터의 값과 단위를 함께 캡슐화하는 임베디드 값 객체(Value Object)입니다.
 * <p>
 * 단독으로 사용되기보다 다른 엔티티의 일부로 포함되어 측정 단위를 명확히 표현하는 데 사용됩니다.
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quantity {
    /**
     * 측정값의 단위
     * <p>예: {@code "m"}, {@code "kg"}, {@code "mmHg"}, {@code "steps"}</p>
     */
    private String unit;

    /**
     * 단위와 연계된 실제 수치 값
     * <p>예: {@code 100.5}, {@code 85.0}</p>
     */
    private Double value;

    /**
     * Quantity 생성자 (Builder 패턴)
     *
     * @param unit  측정 단위 (예: "m", "kg")
     * @param value 측정 수치
     */
    @Builder
    public Quantity(String unit, Double value) {
        this.unit = unit;
        this.value = value;
    }
}
