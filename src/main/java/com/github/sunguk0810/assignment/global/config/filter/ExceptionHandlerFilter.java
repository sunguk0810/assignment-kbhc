package com.github.sunguk0810.assignment.global.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * 필터 체인에서 발생하는 예외를 전역적으로 처리하기 위한 서블릿 필터입니다.
 * <p>
 * 보안 필터(JwtFilter 등)나 기타 서블릿 필터 실행 도중 발생하는 예외를 포착하여
 * {@link HandlerExceptionResolver}에 전달함으로써 일관된 에러 응답을 반환하도록 합니다.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    /**
     * 필터 예외를 Spring MVC의 {@link GlobalExceptionHandler}로 연결해주는 리졸버
     */
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 다음 필터를 실행하고 발생한 예외가 있다면 이를 캐치하여 핸들링합니다.
     *
     * @param request     HTTP 요청
     * @param response    HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("[ExceptionHandlerFilter] Filter Exception Caught = {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
