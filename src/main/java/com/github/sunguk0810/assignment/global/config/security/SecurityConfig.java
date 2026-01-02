package com.github.sunguk0810.assignment.global.config.security;

import com.github.sunguk0810.assignment.global.config.filter.ExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 기반의 웹 보안 설정을 담당하는 구성 클래스입니다.
 * <p>
 * {@link EnableWebSecurity}를 통해 웹 보안을 활성화하며,
 * JWT(Json Web Token) 기반의 인증/인가 처리를 위한 필터 체인 규칙을 정의합니다.
 * </p>
 *
 * @see JwtSecurityConfig
 * @see TokenProvider
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * JWT 토큰 생성 및 검증을 담당하는 프로바이더
     */
    private final TokenProvider tokenProvider;

    /**
     * 인증 실패(401 Unauthorized) 시 예외 처리 핸들러
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 인가 실패(403 Forbidden) 시 예외 처리 핸들러
     */
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    /**
     * HTTP 보안 설정을 구성하고 필터 체인을 생성하는 빈(Bean)입니다.
     * <p>
     * 주요 설정 내용은 다음과 같습니다:
     * </p>
     * <ul>
     * <li><b>기본 보안 비활성화</b>: REST API 특성에 맞춰 CORS 및 CSRF 설정을 비활성화합니다.</li>
     * <li><b>예외 처리</b>: 커스텀 핸들러({@code EntryPoint}, {@code AccessDeniedHandler})를 등록합니다.</li>
     * <li><b>세션 정책</b>: {@link SessionCreationPolicy#STATELESS}를 사용하여 세션을 유지하지 않습니다.</li>
     * <li><b>접근 제어</b>: 로그인, 회원가입 등 인증 API는 허용하고, 그 외 요청은 인증을 요구합니다.</li>
     * <li><b>JWT 설정</b>: {@link JwtSecurityConfig}를 적용하여 JWT 필터를 등록합니다.</li>
     * </ul>
     *
     * @param http Spring Security 설정 빌더 객체
     * @return 구성이 완료된 {@link SecurityFilterChain}
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable());

        http.exceptionHandling(
                e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
        );

        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(
                c -> c
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스 허용
                .anyRequest().authenticated()
        );


        http.apply(new JwtSecurityConfig(tokenProvider, exceptionHandlerFilter));
        return http.build();
    }
}
