package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.RefreshTokenRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.request.UserLoginRequest;
import com.github.sunguk0810.assignment.domain.auth.dto.response.AuthTokenResponse;
import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;
import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.config.security.TokenProvider;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.dto.auth.TokenInfo;
import com.github.sunguk0810.assignment.global.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

// https://github.com/KimuJinsu/spring-security-jwt/blob/main/jwt/src/main/java/com/jwt/demo/controller/AuthController.java
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    @Override
    public Optional<AuthTokenResponse> login(UserLoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authToken);


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        user.updateLastLoginAt();
        userRepository.save(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponse accessToken = tokenProvider.createToken(authentication, true);
        TokenResponse refreshToken = tokenProvider.createAndPersistRefreshToken(authentication);

        AuthTokenResponse tokenResponse = AuthTokenResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(accessToken.getExpiresIn())
                .build();



        return Optional.ofNullable(tokenResponse);
    }

    @Override
    public Optional<TokenResponse> refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        log.info("refreshToken = {}", refreshToken);
        TokenInfo tokenInfo = tokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorType.REFRESH_TOKEN_NOT_FOUND));

        if (isTokenExpired(tokenInfo)) {
            tokenRepository.delete(tokenInfo);
            throw new BusinessException(ErrorType.REFRESH_TOKEN_EXPIRED);
        }
        log.info("refreshToken from Redis. tokenInfo = {}", tokenInfo.getRefreshToken());

        User user = userRepository.findById(tokenInfo.getRecordKey())
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));


        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );


        TokenResponse tokenResponse = tokenProvider.createToken(authentication, true);
        return Optional.ofNullable(tokenResponse);
    }

    public boolean isTokenExpired(TokenInfo tokenInfo){
        return tokenInfo.getExpiryDate().isBefore(LocalDateTime.now());
    }

}
