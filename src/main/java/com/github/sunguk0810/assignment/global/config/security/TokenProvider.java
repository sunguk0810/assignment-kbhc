package com.github.sunguk0810.assignment.global.config.security;

import com.github.sunguk0810.assignment.domain.auth.dto.response.TokenResponse;
import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.entity.UserProfile;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.dto.auth.TokenInfo;
import com.github.sunguk0810.assignment.global.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT(Json Web Token) 생성, 검증 및 파싱을 담당하는 핵심 컴포넌트입니다.
 * <p>
 * 액세스 토큰과 리프레시 토큰의 발급을 수행하며,
 * 발급된 리프레시 토큰의 DB 저장 및 요청 헤더로 들어온 토큰의 유효성 검사를 처리합니다.
 * </p>
 *
 * @see TokenRepository
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider implements InitializingBean {
    private final TokenRepository tokenRepository;

    private static final String AUTHORITIES_KEY = "auth";

    /**
     * JWT 서명에 사용할 비밀 키 (Secret Key)
     * <p>Base64로 인코딩된 문자열이어야 합니다.</p>
     */
    @Value( "${jwt.secret-key}")
    private String secretKey;

    /**
     * 액세스 토큰 유효 시간 (밀리초)
     */
    @Value( "${jwt.access-expiration-time}")
    private Long accessExpirationTime;

    /**
     * 리프레시 토큰 유효 시간 (밀리초)
     */
    @Value( "${jwt.refresh-expiration-time}")
    private Long refreshExpirationTime;

    /**
     * HMAC-SHA 암호화 알고리즘에 사용되는 키 객체
     */
    private Key key;


    /**
     * 빈 초기화 시 비밀 키를 디코딩하여 Key 객체를 설정합니다.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 인증 정보(Authentication)를 기반으로 액세스 토큰 또는 리프레시 토큰을 생성합니다.
     * <p>
     * 토큰 페이로드(Claims)에 사용자 ID, 닉네임, 권한 정보를 포함시킵니다.
     * </p>
     *
     * @param authentication Spring Security 인증 객체
     * @param isAccessToken  생성할 토큰의 타입 (true: Access Token, false: Refresh Token)
     * @return 생성된 JWT 문자열과 만료 시간을 담은 DTO
     */
    public TokenResponse createToken(Authentication authentication, boolean isAccessToken) {
        // 사용자의 권한 정보를 문자열로 변환. 예: ROLE_USER, ROLE_ADMIN
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Long now = (new Date()).getTime();
        Long expiryDate = isAccessToken ? now + accessExpirationTime : now + refreshExpirationTime;
        Date validity = new Date(expiryDate);

        Map<String, String> claims = new HashMap<>();
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            User user = userDetails.getUser();
            UserProfile userProfile = user.getUserProfile();

            claims.put("username", user.getUsername());
            claims.put("recordKey", user.getRecordKey());
            claims.put("nickname", userProfile.getNickname());

        }

        String token = Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .expiration(validity)
                .compact();
        return TokenResponse.builder()
                .token(token)
                .expiresIn(expiryDate - now)
                .build();
    }

    /**
     * 리프레시 토큰을 생성하고 데이터베이스에 저장합니다.
     *
     * @param authentication Spring Security 인증 객체
     * @return 생성된 리프레시 토큰 정보
     */
    public TokenResponse createAndPersistRefreshToken(Authentication authentication) {
        TokenResponse response = this.createToken(authentication, false);

        Long now = (new Date()).getTime();
        Date validity = new Date(now + refreshExpirationTime);
        Instant instant = validity.toInstant();
        LocalDateTime expiryDate = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        TokenInfo.TokenInfoBuilder builder = TokenInfo.builder();

        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            User user = userDetails.getUser();
            builder.recordKey(user.getRecordKey());
        }

        TokenInfo tokenInfo = builder
                .refreshToken(response.getToken())
                .expiryDate(expiryDate)
                .build();

        tokenRepository.save(tokenInfo);

        return response;
    }

    /**
     * JWT 토큰을 파싱하여 Authentication 객체를 생성합니다.
     * <p>
     * 토큰 복호화 후 Claims에서 권한 정보를 추출하여 Spring Security가 이해할 수 있는 형태로 변환합니다.
     * </p>
     *
     * @param token JWT 문자열
     * @return 인증 정보가 담긴 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        // 서명 키를 사용하여 JWT를 파싱하고 클레임을 추출합니다.
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(AUTHORITIES_KEY).toString());
        String recordKey = claims.get("recordKey", String.class);
        String username = claims.get("username", String.class);

        User principal = User.builder()
                .email(claims.getSubject())
                .recordKey(recordKey)
                .username(username)
                .build();
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 문자열
     * @return 유효하면 true, 유효하지 않으면 false
     */
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorType.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is wrong.");
        }

        return false;
    }

}
