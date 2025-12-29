package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.LogoutRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.RefreshTokenRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserLoginRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.response.AuthTokenResponse;
import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;


/**
 * 사용자 인증(로그인) 및 토큰 관리(재발급)를 담당하는 비즈니스 로직 인터페이스입니다.
 * <p>
 * 컨트롤러와 도메인/인프라(Repository, TokenProvider) 계층 사이에서
 * 인증 절차를 조율하고 트랜잭션을 관리합니다.
 * </p>
 */
public interface AuthService {

    /**
     * 사용자 로그인 요청을 처리하고 인증 토큰을 발급합니다.
     * <p>
     * 1. 이메일과 비밀번호 검증 (실패 시 예외 발생)<br>
     * 2. 마지막 로그인 시간 업데이트 (DB 반영)<br>
     * 3. 액세스 토큰 및 리프레시 토큰 생성<br>
     * 4. 리프레시 토큰을 Redis(또는 DB)에 저장
     * </p>
     *
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return 액세스 토큰과 리프레시 토큰이 포함된 응답 DTO
     * @throws com.github.sunguk0810.assignment.global.config.exception.BusinessException
     * 비밀번호 불일치, 존재하지 않는 사용자 등 인증 실패 시 발생
     */
    AuthTokenResponse login(UserLoginRequest request);

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 재발급합니다.
     * <p>
     * 1. 요청된 리프레시 토큰의 유효성 검증 (서명, 만료 여부)<br>
     * 2. Redis(저장소)에 저장된 토큰과 일치하는지 확인<br>
     * 3. 검증 성공 시 새로운 액세스 토큰 발급 (리프레시 토큰은 그대로 유지하거나 로테이션 가능)
     * </p>
     *
     * @param request 리프레시 토큰 요청 정보
     * @return 새로 발급된 액세스 토큰 정보 (만료 시간 포함)
     * @throws com.github.sunguk0810.assignment.global.config.exception.BusinessException
     * 토큰이 만료되었거나(Expired), 위변조되었거나, DB에 없을 경우 발생
     */
    TokenResponse refresh(RefreshTokenRequest request);


    /**
     * 사용자의 로그아웃 요청을 처리합니다.
     * 서버에 저장된 리프레시 토큰을 삭제하여 더 이상 해당 토큰으로 새로운 액세스 토큰을 발급받을 수 없게 합니다.
     *
     * @param recordKey 사용자 구분
     * @param request  리프레시 토큰 정보
     * @return 로그아웃 성공 여부를 반환 (성공 시 true, 실패 시 false)
     */
    Boolean logout(String recordKey, LogoutRequest request);
}
