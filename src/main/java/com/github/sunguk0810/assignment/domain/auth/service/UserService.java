package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.UserRegisterRequest;

/**
 * 사용자 계정 관리(회원가입, 정보 수정, 조회 등)를 담당하는 도메인 서비스 인터페이스입니다.
 * <p>
 * 인증(로그인) 로직을 제외한 순수 사용자 데이터 관리 로직을 정의합니다.
 * </p>
 */
public interface UserService {
    /**
     * 신규 사용자 회원가입을 처리합니다.
     * <p>
     * 1. 이메일 중복 확인 (이미 존재하면 예외 발생)<br>
     * 2. 비밀번호 암호화 (PasswordEncoder 사용)<br>
     * 3. 사용자 엔티티 및 프로필 정보 저장
     * </p>
     *
     * @param request 회원가입에 필요한 정보 (이메일, 비밀번호, 닉네임, 프로필 등)
     * @return 생성된 사용자의 고유 식별 키 (RecordKey 또는 Email)
     * @throws com.github.sunguk0810.assignment.global.config.exception.BusinessException
     * 이미 가입된 이메일인 경우({@code EMAIL_DUPLICATION}) 발생
     */
    String register(UserRegisterRequest request);
}
