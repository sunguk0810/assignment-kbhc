package com.github.sunguk0810.assignment.domain.health.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 건강 데이터 수집에 사용된 디바이스(웨어러블, 모바일 등)의 제조사 또는 플랫폼 유형을 정의합니다.
 * <p>
 * 주로 측정 로그 엔티티에서 데이터의 출처를 구분하거나,
 * 사용자에게 연결된 기기 정보를 표시할 때 사용됩니다.
 * </p>
 */
@Getter
@AllArgsConstructor
public enum DeviceType {
    /**
     * 삼성전자 디바이스 및 플랫폼
     * <p>예: Galaxy Watch, Samsung Health App</p>
     */
    SAMSUNG("삼성전자"),

    /**
     * 애플 디바이스 및 플랫폼
     * <p>예: Apple Watch, HealthKit</p>
     */
    APPLE("Apple"),

    /**
     * 정의되지 않은 디바이스 유형을 나타냅니다.
     * <p>
     * 디바이스 유형이 특정 제조사 또는 플랫폼에 속하지 않거나,
     * 알 수 없는 경우를 나타낼 때 사용됩니다.
     * </p>
     */
    UNDEFINED("정의되지 않은 디바이스");

    private final String description;
}
