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
    CHECKUP_ANALYSIS
}
