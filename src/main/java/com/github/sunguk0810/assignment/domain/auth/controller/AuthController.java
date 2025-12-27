package com.github.sunguk0810.assignment.domain.auth.controller;

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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("register")
    public ApiResponse<String> register(@RequestBody @Valid UserRegisterRequest request){
        String recordKey = userService.register(request);
        return ApiResponse.success(recordKey, "test message");
    }

    @PostMapping("login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response){
        log.info("login request = {}", request);
        Optional<AuthTokenResponse> tokenResponse = authService.login(request);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.get().getAccessToken());
        return ApiResponse.success(tokenResponse.get());
    }

    @PostMapping("refresh-token")
    public ApiResponse<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        Optional<TokenResponse> tokenResponse = authService.refresh(request);

        return ApiResponse.success(tokenResponse.get());
    }
}
