package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.LogoutRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.RefreshTokenRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserLoginRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.response.AuthTokenResponse;
import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.auth.TokenInfo;
import com.github.sunguk0810.assignment.global.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenRepository tokenRepository;

    // 테스트용 정보
    private static final String TS_EMAIL = "admin@admin.com";
    private static final String TS_PASSWORD = "abc123qwe!";
    private static final String TS_RECORD_KEY = "7836887b-b12a-440f-af0f-851546504b13";

    private static final UserLoginRequest TS_EXIST_USER_REQUEST;

    static {
        TS_EXIST_USER_REQUEST = UserLoginRequest.builder()
                .email(TS_EMAIL)
                .password(TS_PASSWORD)
                .build();
    }

    // Redis는 @Transaction 롤백이 적용되지 않아. 각각 테스트 케이스 실행시 tearDown 메서드를 수행
    @AfterEach
    void tearDown(){
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("[TS-AUTH-01] 로그인 성공")
    public void authService_로그인_성공(){
        // given & when
        AuthTokenResponse response = authService.login(TS_EXIST_USER_REQUEST);

        // then
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    @DisplayName("[TS-AUTH-02] 토큰 재발급 성공")
    public void authService_토큰_재발급_성공(){
        // given
        AuthTokenResponse response = authService.login(TS_EXIST_USER_REQUEST);
        String refreshToken = response.getRefreshToken();

        RefreshTokenRequest tokenRequest = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        // when
        TokenResponse tokenResponse = authService.refresh(tokenRequest);

        // then
        assertNotNull(tokenResponse.getToken());
    }

    @Test
    @DisplayName("[TS-AUTH-03] 토큰 재발급 실패")
    public void authService_토큰_재발급_실패(){
        // given
        AuthTokenResponse response = authService.login(TS_EXIST_USER_REQUEST);

        String refreshToken = response.getRefreshToken();
        LogoutRequest logoutRequest = LogoutRequest.builder()
                .refreshToken(refreshToken)
                .build();

        authService.logout(TS_RECORD_KEY, logoutRequest);

        RefreshTokenRequest tokenRequest = RefreshTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();

        // when & then
        assertThatThrownBy(() -> authService.refresh(tokenRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorType.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[TS-AUTH-04] 로그아웃")
    public void authService_로그아웃(){
        // given
        AuthTokenResponse response = authService.login(TS_EXIST_USER_REQUEST);

        String refreshToken = response.getRefreshToken();
        LogoutRequest logoutRequest = LogoutRequest.builder()
                .refreshToken(refreshToken)
                .build();

        // when
        authService.logout(TS_RECORD_KEY, logoutRequest);

        // then
        Optional<TokenInfo> tokenInfo = tokenRepository.findByRefreshToken(refreshToken);
        assertFalse(tokenInfo.isPresent());

    }

}