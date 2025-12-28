package com.github.sunguk0810.assignment.domain.auth.controller;

import com.github.sunguk0810.assignment.domain.auth.dto.request.LogoutRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.RefreshTokenRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserLoginRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserRegisterRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.response.AuthTokenResponse;
import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;
import com.github.sunguk0810.assignment.domain.auth.service.AuthService;
import com.github.sunguk0810.assignment.domain.auth.service.UserService;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    /**
     * 신규 회원 가입을 처리합니다.
     * <p>
     * [POST] /api/v1/auth/register
     * </p>
     *
     * @param request 회원가입에 필요한 정보(이메일, 비밀번호, 프로필 등)
     * @return 가입된 사용자의 식별 키(Record Key)와 성공 메시지
     */
    @PostMapping("register")
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request){
        String recordKey = userService.register(request);
        return ApiResponse.success(recordKey, "회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 사용자 로그인 및 인증 토큰 발급을 처리합니다.
     * <p>
     * [POST] /api/v1/auth/login<br>
     * 인증 성공 시 액세스 토큰과 리프레시 토큰을 응답 바디에 포함하며,
     * 편의를 위해 Authorization 헤더에도 액세스 토큰을 추가합니다.
     * </p>
     *
     * @param request  로그인 요청 정보(이메일, 비밀번호)
     * @param response HTTP 응답 객체 (헤더 설정용)
     * @return 액세스 토큰 및 리프레시 토큰 정보
     */
    @PostMapping("login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response){
        log.info("[Login Request] email: {}", request.getEmail());
        AuthTokenResponse tokenResponse = authService.login(request);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken());
        return ApiResponse.success(tokenResponse, "로그인되었습니다.");
    }

    /**
     * 액세스 토큰 만료 시 리프레시 토큰을 사용하여 토큰을 재발급합니다.
     * <p>
     * [POST] /api/v1/auth/refresh-token
     * </p>
     *
     * @param request 리프레시 토큰을 포함한 요청 객체
     * @return 새로 발급된 액세스 토큰 정보
     */
    @PostMapping("refresh-token")
    public ApiResponse<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenResponse tokenResponse = authService.refresh(request);
        return ApiResponse.success(tokenResponse, "토큰이 갱신되었습니다.");
    }

    /**
     * 로그아웃 처리를 수행합니다. (리프레시 토큰 무효화)
     * <p>
     * [POST] /api/v1/auth/logout<br>
     * 서버 측 Redis에 저장된 리프레시 토큰을 삭제하여 더 이상 토큰 갱신이 불가능하게 만듭니다.
     * </p>
     *
     * @param request 로그아웃할 리프레시 토큰 정보
     * @return 데이터 없는 성공 응답
     */
    @PostMapping("logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request){
        authService.logout(request);
        return ApiResponse.success(null, "로그아웃이 완료되었습니다.");
    }
}
