package com.github.sunguk0810.assignment.domain.auth.controller;

import com.github.sunguk0810.assignment.domain.auth.dto.request.LogoutRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.RefreshTokenRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserLoginRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserRegisterRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.response.AuthTokenResponse;
import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;
import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.service.AuthService;
import com.github.sunguk0810.assignment.domain.auth.service.UserService;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


/**
 * 인증 및 회원 관리를 위한 REST 컨트롤러입니다.
 * <p>
 * 회원가입, 로그인, 토큰 갱신, 로그아웃 등
 * 사용자의 계정 인증과 관련된 기능을 제공합니다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    /**
     * 신규 회원 가입을 처리합니다.
     * <p>
     * 사용자의 이메일, 비밀번호 및 프로필 정보를 입력받아
     * 새로운 계정을 생성하고 고유 식별 키(Record Key)를 발급합니다.
     * </p>
     *
     * @param request 회원가입에 필요한 정보 (이메일, 비밀번호, 성별, 생년월일 등)
     * @return 생성된 사용자의 고유 식별 키 (UUID)
     */
    @PostMapping("register")
    @Operation(summary = "회원가입", description = "신규 사용자를 등록합니다.")
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request){
        String recordKey = userService.register(request);
        return ApiResponse.success(recordKey, "회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 사용자 인증을 수행하고 JWT 토큰을 발급합니다.
     * <p>
     * 이메일과 비밀번호를 검증하여 유효한 사용자인 경우,
     * 액세스 토큰(Access Token)과 리프레시 토큰(Refresh Token)을 반환합니다.
     * </p>
     *
     * @param request  로그인 요청 정보 (이메일, 비밀번호)
     * @param response HTTP 응답 객체 (Authorization 헤더 설정을 위해 사용)
     * @return 발급된 토큰 세트 (액세스 토큰 및 리프레시 토큰)
     */
    @PostMapping("login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 인증하고 토큰을 발급받습니다.")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response){
        log.info("[Login Request] email: {}", request.getEmail());
        AuthTokenResponse tokenResponse = authService.login(request);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken());
        return ApiResponse.success(tokenResponse, "로그인되었습니다.");
    }

    /**
     * 만료된 액세스 토큰을 리프레시 토큰을 사용하여 재발급합니다.
     * <p>
     * 유효한 리프레시 토큰이 확인되면 새로운 액세스 토큰을 생성하여 반환합니다.
     * </p>
     *
     * @param request 리프레시 토큰 정보를 담은 DTO
     * @return 새로 생성된 액세스 토큰
     */
    @PostMapping("refresh-token")
    @Operation(summary = "토큰 재발급", description = "만료된 Access Token을 Refresh Token을 통해 갱신합니다.")
    public ApiResponse<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        TokenResponse tokenResponse = authService.refresh(request);
        return ApiResponse.success(tokenResponse, "토큰이 갱신되었습니다.");
    }

    /**
     * 사용자의 로그아웃 처리를 수행합니다.
     * <p>
     * 서버에 저장된 리프레시 토큰을 무효화하여 더 이상 토큰 갱신이 불가능하도록 처리합니다.
     * </p>
     *
     * @param request   리프레시 토큰 정보를 포함한 로그아웃 요청 DTO
     * @param principal 현재 인증된 사용자의 Principal 객체
     * @return 성공 메시지
     * @throws BusinessException {@link ErrorType#USER_NOT_FOUND} 인증 정보가 없는 경우 발생
     */
    @PostMapping("logout")
    @Operation(summary = "로그아웃", description = "Refresh Token을 무효화하여 로그아웃 처리합니다.")
    public ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request, Principal principal){
        if (principal == null){
            throw new BusinessException(ErrorType.USER_NOT_FOUND);
        }
        if (principal instanceof UsernamePasswordAuthenticationToken token){
            if (token.getPrincipal() != null && token.getPrincipal() instanceof User user){
                String recordKey = user.getRecordKey();
                authService.logout(recordKey, request);
            }
        }
        return ApiResponse.success(null, "로그아웃이 완료되었습니다.");
    }
}
