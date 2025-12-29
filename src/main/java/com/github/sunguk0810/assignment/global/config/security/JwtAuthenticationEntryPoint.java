package com.github.sunguk0810.assignment.global.config.security;

import com.github.sunguk0810.assignment.global.config.filter.JwtFilter;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 인증되지 않은 사용자(Unauthenticated)가 보호된 리소스에 접근할 때 호출되는 핸들러입니다.
 * <p>
 * Spring Security 필터 체인에서 인증 실패 시(예: 유효한 토큰 없음, 토큰 만료 등) 실행되며,
 * 클라이언트에게 401 Unauthorized 상태 코드와 공통 에러 포맷(JSON)을 반환합니다.
 * </p>
 *
 * @see JwtFilter
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 응답 객체를 JSON 문자열로 변환하기 위한 ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 호출되는 메서드입니다.
     * <p>
     * HTTP 응답 상태를 401로 설정하고, {@link ApiResponse}를 사용하여 에러 메시지를 JSON 형태로 출력 스트림에 작성합니다.
     * </p>
     *
     * @param request       HttpServletRequest 객체
     * @param response      HttpServletResponse 객체
     * @param authException 인증 과정에서 발생한 예외 (구체적인 실패 원인 포함)
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        log.error("[Unauthorized] Request URI: {}, Message: {}", request.getRequestURI(), authException.getMessage());
        ApiResponse<Void> errorResponse = ApiResponse.fail("인증 정보가 유효하지 않습니다. (" + authException.getMessage() + ")");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
