package com.github.sunguk0810.assignment.global.config.exception;

import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 애플리케이션 전역에서 발생하는 예외를 감지하고 처리하는 핸들러입니다.
 * <p>
 * {@link org.springframework.web.bind.annotation.RestController}에서 발생하는 예외를 가로채어
 * 표준 응답 포맷({@link ApiResponse})으로 변환하여 클라이언트에게 반환합니다.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 수행 중 의도적으로 발생시킨 예외를 처리합니다.
     * <p>
     * {@link BusinessException}에 정의된 {@link ErrorType}을 기반으로
     * HTTP 상태 코드와 커스텀 에러 메시지를 반환합니다.
     * </p>
     *
     * @param e 발생한 비즈니스 예외
     * @return ErrorType에 정의된 상태 코드와 메시지를 포함한 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorType errorType = e.getErrorType();
        ApiResponse<Void> response = ApiResponse.fail(errorType.getMessage(), errorType.getCode());

        return ResponseEntity
                .status(errorType.getStatus())

                .body(response);
    }


    /**
     * 존재하지 않는 API 경로(URL)를 호출했을 때 발생하는 예외를 처리합니다.
     * <p>
     * Spring Boot 3.x 이상에서는 정적 리소스나 매핑된 URL을 찾을 수 없을 때
     * {@link NoResourceFoundException}이 발생합니다.
     * </p>
     *
     * @param ex 리소스를 찾을 수 없음 예외
     * @return 404 Not Found 응답
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<Void> response = ApiResponse.fail("요청한 경로를 찾을 수 없습니다.");

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * {@code @Valid} 유효성 검증 실패 시 발생하는 예외를 처리합니다.
     * <p>
     * 여러 필드에서 발생한 에러를 필드별로 그룹화하여 Map 형태로 반환합니다.
     * (예: {@code "email": ["형식이 올바르지 않습니다"], "password": ["..."]})
     * </p>
     *
     * @param ex 유효성 검증 실패 예외
     * @return 400 Bad Request 및 필드별 에러 상세 내역
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        // 필드별로 에러 메시지를 리스트로 모음
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "유효하지 않은 값입니다.",
                                Collectors.toList()
                        )
                ));

        ApiResponse<Map<String, List<String>>> response = ApiResponse.fail("값 검증에 실패하였습니다.", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 위에서 처리되지 않은 기타 모든 예외를 처리합니다. (최후의 보루)
     * <p>
     * 서버 내부 오류(500)로 간주하며, 로그를 남기고 클라이언트에게는 일반적인 에러 메시지를 전달합니다.
     * </p>
     *
     * @param e 처리되지 않은 일반 예외
     * @return 500 Internal Server Error 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("handleException {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.fail(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
