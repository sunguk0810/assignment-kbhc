package com.github.sunguk0810.assignment.domain.auth.constant;

/**
 * 사용자 권한 유형(Role Type)을 정의하는 열거형 클래스입니다.
 *
 * 사용자 계정 및 인증과 관련된 역할을 구분하기 위해 사용됩니다.
 * 각 권한 유형은 시스템에서 수행할 수 있는 작업의 범위와 접근 가능한 리소스를 제한하거나 허용하는 데 사용됩니다.
 */
public enum RoleType {
    /**
     * 일반 사용자 (User)
     * <p>서비스의 기본적인 기능을 사용할 수 있는 표준 권한입니다.</p>
     */
    ROLE_USER,

    /**
     * 시스템 관리자 (Admin)
     * <p>시스템 설정, 회원 관리 등 운영에 필요한 모든 기능에 접근할 수 있는 관리자 권한입니다.</p>
     */
    ROLE_ADMIN,

    /**
     * 익명 사용자 (Anonymous)
     *
     * 인증되지 않은 사용자를 나타내는 권한 유형입니다.
     * 주로 시스템 내 공용 리소스에 접근할 수 있는 제한된 권한으로 사용됩니다.
     */
    ROLE_ANONYMOUS
}
