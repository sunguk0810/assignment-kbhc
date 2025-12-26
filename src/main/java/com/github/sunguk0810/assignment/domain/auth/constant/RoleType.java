package com.github.sunguk0810.assignment.domain.auth.constant;

import com.github.sunguk0810.assignment.domain.auth.entity.Role;
import com.github.sunguk0810.assignment.domain.auth.entity.UserRole;

/**
 * 시스템 내 사용자의 접근 권한(Authority) 유형을 정의하는 열거형입니다.
 * <p>
 * 보안 프레임워크(예: Spring Security)의 표준 명명 규칙({@code ROLE_} prefix)을 따르며,
 * {@link Role} 및 {@link UserRole} 엔티티에서 권한 관리를 위한 기준으로 사용됩니다.
 * </p>
 *
 * @see Role
 * @see UserRole
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
    ROLE_ADMIN
}
