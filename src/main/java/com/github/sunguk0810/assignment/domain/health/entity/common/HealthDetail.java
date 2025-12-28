package com.github.sunguk0810.assignment.domain.health.entity.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.sunguk0810.assignment.domain.health.dto.measure.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 다양한 건강 측정 데이터(걸음 수, 혈압 등)를 추상화한 상위 인터페이스입니다.
 * <p>
 * Jackson 라이브러리의 다형성 처리 기능({@link JsonTypeInfo})을 사용하여,
 * JSON 데이터의 {@code "type"} 속성 값에 따라 적절한 하위 클래스로 역직렬화됩니다.
 * </p>
 *
 * <h3>지원하는 데이터 타입</h3>
 * <ul>
 * <li>{@link Step} : 걸음 수 데이터</li>
 * <li>{@link BloodPressure} : 혈압 데이터</li>
 * <li>{@link HeartRate} : 심박수 데이터</li>
 * <li>{@link OxygenSaturation} : 산소포화도 데이터</li>
 * <li>{@link BloodSugar} : 혈당 데이터</li>
 * </ul>
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
}
