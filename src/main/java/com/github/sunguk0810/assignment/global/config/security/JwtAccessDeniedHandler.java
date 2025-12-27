package com.github.sunguk0810.assignment.global.config.security;

import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * 인증된 사용자(Authenticated)가 필요한 권한(Authority)을 보유하지 않은 상태로
 * 보호된 리소스에 접근하려 할 때 호출되는 핸들러입니다.
 * <p>
 * Spring Security 필터 체인에서 인가(Authorization) 실패 시 실행되며,
 * 클라이언트에게 403 Forbidden 상태 코드와 공통 에러 포맷(JSON)을 반환합니다.
 * </p>
 *
 * @see com.github.sunguk0810.assignment.global.config.security.JwtAuthenticationEntryPoint
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 응답 객체를 JSON 문자열로 변환하기 위한 ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 접근 권한이 부족한 경우(403 Forbidden) 호출되는 메서드입니다.
     * <p>
     * HTTP 응답 상태를 403으로 설정하고, {@link ApiResponse}를 사용하여 에러 메시지를 JSON 형태로 반환합니다.
     * </p>
     *
     * @param request               HttpServletRequest 객체
     * @param response              HttpServletResponse 객체
     * @param accessDeniedException 권한 거부 원인을 담은 예외 객체
     * @throws IOException      입출력 예외 발생 시
     * @throws ServletException 서블릿 처리 중 예외 발생 시
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 권한 부족 상황을 로그로 남겨 보안 모니터링이나 디버깅에 활용합니다.
        log.warn("[Forbidden] Request URI: {}, Message: {}", request.getRequestURI(), accessDeniedException.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

// 클라이언트에게 명확한 실패 사유 전달
        ApiResponse<Void> errorResponse = ApiResponse.fail("접근 권한이 없습니다. (" + accessDeniedException.getMessage() + ")");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
