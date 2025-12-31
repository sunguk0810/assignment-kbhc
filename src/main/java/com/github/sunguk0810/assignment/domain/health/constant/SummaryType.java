package com.github.sunguk0810.assignment.domain.health.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;

import java.util.Arrays;

/**
 * 건강 데이터 요약(Summary) 조회 시, <b>데이터 집계 단위(기간)</b>를 정의하는 열거형입니다.
 * <p>
 * 클라이언트가 요청하는 기간의 성격에 따라 <b>일간(Daily)</b> 또는 <b>월간(Monthly)</b>으로 구분됩니다.
 * Jackson 라이브러리의 {@link JsonAlias}를 사용하여, 대소문자 구분 없이 다양한 입력값에 대해 유연한 역직렬화(Deserialization)를 지원합니다.
 * </p>
 *
 * @see JsonAlias
 */
public enum SummaryType {

    /**
     * <b>일간 요약 (Daily Summary)</b>
     * <p>
     * 특정 일자(Day)를 기준으로 데이터를 집계하여 반환합니다.
     * </p>
     * <ul>
     * <li>허용되는 JSON 입력값: {@code "daily"}, {@code "DAILY"}</li>
     * </ul>
     */
    @JsonAlias({"daily", "DAILY"})
    DAILY(Values.DAILY),

    /**
     * <b>월간 요약 (Monthly Summary)</b>
     * <p>
     * 특정 월(Month)을 기준으로 데이터를 집계하여 반환합니다.
     * </p>
     * <ul>
     * <li>허용되는 JSON 입력값: {@code "monthly"}, {@code "MONTHLY"}</li>
     * </ul>
     */
    @JsonAlias({"monthly", "MONTHLY"})
    MONTHLY(Values.MONTHLY);

    private final String type;


    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static SummaryType from(String value){
        return Arrays.stream(SummaryType.values())
                .filter(v -> v.getType().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorType.INVALID_PARAMETER));
    }

    SummaryType(String type) {
        this.type = type;
    }
    public static class Values {
        public static final String DAILY = "DAILY";
        public static final String MONTHLY = "MONTHLY";
    }
}