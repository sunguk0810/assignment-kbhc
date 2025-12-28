package com.github.sunguk0810.assignment.domain.health.constant;

/**
 * 건강 데이터의 원천(Source)을 구분하는 열거형입니다.
 * <p>
 * 데이터가 어떤 플랫폼, 디바이스, 또는 외부 시스템으로부터 수집되었는지를 정의합니다.
 * </p>
 *
 * @see com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureLog
 */
public enum DataSourceType {
    /**
     * 삼성 헬스 (Samsung Health)
     * <p>Galaxy Watch 또는 삼성 헬스 앱을 통해 수집된 데이터입니다.</p>
     */
    HEALTH_SAMSUNG,

    /**
     * 애플 건강 (Apple Health)
     * <p>Apple Watch 또는 iOS 건강 앱(HealthKit)을 통해 수집된 데이터입니다.</p>
     */
    HEALTH_APPLE,

    /**
     * 인바디 (InBody)
     * <p>InBody 기기를 통해 측정된 체성분 분석 데이터입니다.</p>
     */
    INBODY,

    /**
     * 건강검진 분석 (Checkup Analysis)
     * <p>국가 건강검진 또는 병원 검진 데이터를 바탕으로 분석된 결과입니다.</p>
     */
    CHECKUP_ANALYSIS,

    /**
     * 정의되지 않은 데이터 원천을 나타냅니다.
     * <p>
     * 데이터 소스가 명확히 식별되지 않거나, 설정되지 않았을 경우 사용됩니다.
     * 데이터의 출처를 특정할 수 없는 상황을 처리하기 위한 기본값으로 활용됩니다.
     * </p>
     */
    UNDEFINED
    ;

    /**
     * 주어진 문자열을 기반으로 데이터 소스 유형({@link DataSourceType})을 반환합니다.
     * <p>
     * 데이터를 수집한 플랫폼(삼성 헬스, 애플 건강 등)을 문자열로 입력받아 해당하는 {@link DataSourceType} 열거형 값을 반환하며,
     * 입력된 문자열이 정의된 값에 매칭되지 않을 경우 기본값으로 {@link DataSourceType#UNDEFINED}를 반환합니다.
     * </p>
     *
     * @param source 데이터 소스를 나타내는 문자열 (예: "SamsungHealth", "Health Kit", "InBody", "CODEF")
     * @return 매칭되는 {@link DataSourceType} 열거형 값
     */
    public static DataSourceType from(String source) {

        switch (source) {
            case "SamsungHealth" -> {return DataSourceType.HEALTH_SAMSUNG;}
            case "Health Kit" -> {return DataSourceType.HEALTH_APPLE;}
            case "InBody" -> {return DataSourceType.INBODY;}
            case "CODEF" -> {return DataSourceType.CHECKUP_ANALYSIS;}
            default -> {return DataSourceType.UNDEFINED;}
        }
    }
}
