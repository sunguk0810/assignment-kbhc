package com.github.sunguk0810.assignment.domain.health.constant;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * 건강 측정 데이터의 유형을 정의하는 열거형입니다.
 * <p>
 * 각 열거형 상수는 고유한 문자열 값을 가지며, Jackson 라이브러리({@link JsonValue}, {@link JsonCreator})를 통해
 * JSON 데이터와 상호 변환(직렬화/역직렬화)을 지원합니다.
 * </p>
 */
public enum MeasureType {
    /**
     * 걸음 수 (Steps)
     * <p>매핑 값: {@code "STEPS"}</p>
     */
    STEPS(Values.STEPS),

    /**
     * 산소 포화도 (Oxygen Saturation)
     * <p>매핑 값: {@code "OXYGEN_SATURATION"}</p>
     */
    OXYGEN_SATURATION(Values.OXYGEN_SATURATION),

    /**
     * 혈압 (Blood Pressure)
     * <p>매핑 값: {@code "BLOOD_PRESSURE"}</p>
     */
    BLOOD_PRESSURE(Values.BLOOD_PRESSURE),

    /**
     * 혈당 (Blood Sugar)
     * <p>매핑 값: {@code "BLOOD_SUGAR"}</p>
     */
    BLOOD_SUGAR(Values.BLOOD_SUGAR),

    /**
     * 심박수 (Heart Rate)
     * <p>매핑 값: {@code "HEART_RATE"}</p>
     */
    HEART_RATE(Values.HEART_RATE),

    /**
     * 운동 데이터 (Exercise)
     * <p>매핑 값: {@code "EXERCISE"}</p>
     */
    EXERCISE(Values.EXERCISE);

    private final String type;

    /**
     * Enum 상수에 대응하는 JSON 문자열 값을 반환합니다.
     * <p>
     * Jackson 라이브러리가 객체를 JSON으로 직렬화할 때 이 값을 사용합니다.
     * </p>
     *
     * @return 측정 타입 문자열 (예: "STEPS")
     */
    @JsonValue
    public String getType() {
        return type;
    }

    /**
     * JSON 문자열 값을 받아 해당하는 {@code MeasureType} 상수를 반환합니다.
     * <p>
     * 대소문자를 구분하지 않고 매칭하며, Jackson 라이브러리가 JSON을 객체로 역직렬화할 때 사용합니다.
     * </p>
     *
     * @param value JSON 문자열 값
     * @return 해당하는 {@link MeasureType} 상수
     * @throws IllegalArgumentException 정의되지 않은 타입 문자열이 입력된 경우 발생
     */
    @JsonCreator
    public static MeasureType from(String value){
      return Arrays.stream(MeasureType.values())
                .filter(v -> v.getType().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 타입입니다."));
    }

    MeasureType(String type) {
        this.type = type;
    }

    public static class Values {
        public static final String STEPS = "STEPS";
        public static final String OXYGEN_SATURATION = "OXYGEN_SATURATION";
        public static final String BLOOD_PRESSURE = "BLOOD_PRESSURE";
        public static final String BLOOD_SUGAR = "BLOOD_SUGAR";
        public static final String HEART_RATE = "HEART_RATE";
        public static final String EXERCISE = "EXERCISE";
    }


}
