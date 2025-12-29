package com.github.sunguk0810.assignment.domain.auth.service;

import com.github.sunguk0810.assignment.domain.auth.dto.request.LogoutRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public AuthTokenResponse login(UserLoginRequest request) {
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

        return AuthTokenResponse.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(accessToken.getExpiresIn())
                .build();
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
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
        return tokenProvider.createToken(authentication, true);
    }

    public boolean isTokenExpired(TokenInfo tokenInfo){
        return tokenInfo.getExpiryDate().isBefore(LocalDateTime.now());
    }

    @Override
    public Boolean logout(String recordKey, LogoutRequest request) {
        TokenInfo tokenInfo = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException(ErrorType.REFRESH_TOKEN_NOT_FOUND));

        if (!tokenInfo.getRecordKey().equals(recordKey)){
            throw new BusinessException(ErrorType.UNAUTHORIZED);
        }
        tokenRepository.delete(tokenInfo);
        return true;
    }
}
