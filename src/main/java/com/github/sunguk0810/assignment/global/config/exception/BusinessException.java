package com.github.sunguk0810.assignment.global.config.exception;

import com.github.sunguk0810.assignment.global.constant.ErrorType;
import lombok.Getter;

/**
 * 비즈니스 로직 수행 중 발생하는 예외를 처리하기 위한 커스텀 RuntimeException입니다.
 * <p>
 * {@link ErrorType}을 포함하여 예외의 종류, HTTP 상태 코드, 기본 메시지를 캡슐화합니다.
 * 이를 통해 일관된 에러 응답 포맷을 유지할 수 있습니다.
 * </p>
 *
 * @see ErrorType
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 발생한 예외의 구체적인 유형(Error Code, Message, HTTP Status)을 정의하는 필드입니다.
     */
    private final ErrorType errorType;

    /**
     * ErrorType만으로 예외를 생성합니다.
     * <p>
     * 메시지는 ErrorType에 정의된 기본 메시지를 사용합니다.
     * </p>
     *
     * @param errorType 예외 유형 ({@link ErrorType})
     */
    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    /**
     * ErrorType과 함께 원인 예외(Cause)를 래핑하여 생성합니다.
     * <p>
     * 다른 예외(예: IOException, SQLException)를 잡아서 비즈니스 예외로 변환할 때 유용합니다.
     * 메시지는 ErrorType의 기본 메시지를 사용합니다.
     * </p>
     *
     * @param errorType 예외 유형
     * @param cause     원인이 되는 하위 예외
     */
    public BusinessException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
    }
    /**
     * ErrorType, 커스텀 메시지, 원인 예외를 모두 지정하여 생성합니다.
     * <p>
     * ErrorType의 기본 메시지 대신 더 구체적인 상황 설명이 필요할 때 사용합니다.
     * </p>
     *
     * @param errorType 예외 유형
     * @param message   상세 에러 메시지 (ErrorType 메시지 덮어씀)
     * @param cause     원인이 되는 하위 예외
     */
    public BusinessException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
}
