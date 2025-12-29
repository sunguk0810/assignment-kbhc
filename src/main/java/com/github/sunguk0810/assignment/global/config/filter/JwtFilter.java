package com.github.sunguk0810.assignment.global.config.filter;

import com.github.sunguk0810.assignment.global.config.security.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * HTTP 요청의 헤더에서 JWT를 추출하고 유효성을 검증하는 보안 필터입니다.
 * <p>
 * Spring Security 필터 체인 상에 위치하며, 토큰이 유효할 경우
 * {@link Authentication} 객체를 생성하여 {@link SecurityContext}에 저장합니다.
 * 이 과정을 통해 이후의 요청 처리 과정에서 인증된 사용자 정보를 사용할 수 있게 됩니다.
 * </p>
 *
 * @see TokenProvider
 * @see SecurityContextHolder
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    /**
     * 인증 토큰을 전달하는 표준 HTTP 헤더 키
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 토큰 생성, 검증 및 파싱을 담당하는 컴포넌트
     */
    private final TokenProvider tokenProvider;

    /**
     * 필터링 로직을 수행하는 메서드입니다.
     * <p>
     * 1. 요청 헤더에서 JWT 토큰 추출<br>
     * 2. 토큰 유효성 검증<br>
     * 3. 유효 시 SecurityContext에 인증 정보(Authentication) 저장
     * </p>
     *
     * @param servletRequest  들어오는 요청 객체
     * @param servletResponse 나가는 응답 객체
     * @param filterChain     다음 필터로 요청을 전달하기 위한 체인 객체
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        log.debug("[JwtFilter] Processing Request URI: {}, JWT Present: {}", requestURI, StringUtils.hasText(jwt));

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("[JwtFilter] Authentication set for user: {}, URI: {}", authentication.getName(), requestURI);
        } else {
            log.debug("[JwtFilter] No valid JWT token found, URI: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * HTTP 요청 헤더에서 'Bearer ' 접두사를 제거하고 토큰 본문만 추출합니다.
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 JWT 문자열, 없으면 null 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
