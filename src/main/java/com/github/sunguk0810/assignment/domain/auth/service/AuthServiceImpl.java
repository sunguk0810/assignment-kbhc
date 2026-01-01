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

/**
 * 사용자 인증 및 토큰 관리 비즈니스 로직을 처리하는 서비스 구현체입니다.
 * <p>
 * 로그인, 토큰 갱신, 로그아웃 등 인증과 관련된 핵심 기능을 수행하며,
 * Spring Security 및 JWT Provider와 연동하여 보안 처리를 담당합니다.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 사용자 로그인을 처리하고 인증 토큰을 발급합니다.
     * <p>
     * 이메일과 비밀번호 기반의 인증 토큰을 생성하여 검증을 수행하고,
     * 인증 성공 시 액세스 토큰과 리프레시 토큰을 생성하여 반환합니다.
     * 또한, 사용자의 마지막 로그인 시간을 갱신합니다.
     * </p>
     *
     * @param request 로그인 요청 정보 (이메일, 비밀번호)
     * @return 발급된 액세스 토큰 및 리프레시 토큰 정보를 담은 응답 객체
     */
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

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
     * <p>
     * 저장소에 저장된 리프레시 토큰의 유효성과 만료 여부를 검증한 후,
     * 유효한 경우 새로운 액세스 토큰을 생성하여 반환합니다.
     * 만료된 토큰인 경우 저장소에서 삭제하고 예외를 발생시킵니다.
     * </p>
     *
     * @param request 리프레시 토큰 정보가 담긴 요청 객체
     * @return 새로 발급된 액세스 토큰 정보
     * @throws BusinessException {@link ErrorType#REFRESH_TOKEN_NOT_FOUND} 토큰이 존재하지 않을 경우,
     *                           {@link ErrorType#REFRESH_TOKEN_EXPIRED} 토큰이 만료된 경우
     */
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

    /**
     * 토큰의 만료 여부를 확인합니다.
     *
     * @param tokenInfo 검사할 토큰 정보
     * @return 만료되었으면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean isTokenExpired(TokenInfo tokenInfo){
        return tokenInfo.getExpiryDate().isBefore(LocalDateTime.now());
    }

    /**
     * 로그아웃 처리를 수행하여 리프레시 토큰을 무효화합니다.
     * <p>
     * 요청된 리프레시 토큰이 해당 사용자의 것인지 검증한 후,
     * 저장소에서 삭제하여 더 이상 사용할 수 없도록 합니다.
     * </p>
     *
     * @param recordKey 요청한 사용자의 식별 키
     * @param request   로그아웃 요청 정보 (리프레시 토큰 포함)
     * @return 로그아웃 성공 여부 (항상 {@code true})
     * @throws BusinessException {@link ErrorType#UNAUTHORIZED} 토큰 소유자가 일치하지 않을 경우
     */
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
