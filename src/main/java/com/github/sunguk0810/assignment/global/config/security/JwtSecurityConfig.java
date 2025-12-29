package com.github.sunguk0810.assignment.global.config.security;

import com.github.sunguk0810.assignment.global.config.filter.ExceptionHandlerFilter;
import com.github.sunguk0810.assignment.global.config.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정에 JWT 관련 구성을 적용하기 위한 설정 어댑터입니다.
 * <p>
 * {@link SecurityConfigurerAdapter}를 확장하여 구현되었으며,
 * 직접 구현한 {@link JwtFilter}를 Spring Security 필터 체인에 등록하는 역할을 담당합니다.
 * </p>
 *
 * @see JwtFilter
 * @see TokenProvider
 */
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * JwtFilter에 주입하기 위한 토큰 제공자
     */
    private final TokenProvider tokenProvider;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    /**
     * HttpSecurity 빌더를 통해 커스텀 보안 설정을 적용합니다.
     * <p>
     * {@link JwtFilter}를 생성하여 {@link UsernamePasswordAuthenticationFilter}보다 앞에 위치시킵니다.
     * 이를 통해 로그인 프로세스가 수행되기 전에 JWT 토큰 유효성 검사가 먼저 이루어지도록 설정합니다.
     * </p>
     *
     * @param http Spring Security 설정을 위한 HttpSecurity 객체
     */
    @Override
    public void configure(HttpSecurity http) {
        http
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
    }
}
