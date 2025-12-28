package com.github.sunguk0810.assignment.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 애플리케이션 전역에서 발생하는 예외 상황을 정의한 열거형(Enum) 클래스입니다.
 * <p>
 * 비즈니스 로직 처리 중 발생하는 예외({@link com.github.sunguk0810.assignment.global.config.exception.BusinessException})에
 * 대한 표준화된 응답 포맷(HTTP 상태 코드, 커스텀 에러 코드, 메시지)을 제공합니다.
 * </p>
 *
 * <h3>분류 체계</h3>
 * <ul>
 * <li><b>C (Common)</b>: 전역적으로 발생하는 공통 에러</li>
 * <li><b>U (User)</b>: 사용자 도메인 관련 에러</li>
 * </ul>
 */
@Getter
@AllArgsConstructor
public enum ErrorType {
    // 공통
    /**
     * 잘못된 요청값(Parameter)이 전달된 경우
     * <p>HTTP 400 Bad Request</p>
     */
    INVALID_PARAMETER(400, "C001", "잘못된 요청입니다."),

    /**
     * 요청한 리소스(URL 등)를 찾을 수 없는 경우
     * <p>HTTP 404 Not Found</p>
     */
    NOT_FOUND(404, "C002", "리소스를 찾을 수 없습니다."),

    /**
     * 서버 내부에서 예상치 못한 오류가 발생한 경우 (핸들링되지 않은 에러)
     * <p>HTTP 500 Internal Server Error</p>
     */
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류입니다."),


    /**
     * JSON 형식이 올바르지 않은 경우
     * <p>HTTP 500 Internal Server Error</p>
     */
    INVALID_JSON_FORMAT(500, "C004", "JSON 형식이 유효하지 않습니다."),


    /**
     * 로그 데이터를 찾을 수 없는 경우
     * <p>HTTP 404 Not Found</p>
     */
    LOG_NOT_FOUND(404, "C005", "로그 데이터를 찾을 수 없습니다."),
    /**
     * 조회하려는 사용자가 존재하지 않는 경우
     * <p>HTTP 404 Not Found</p>
     */
    USER_NOT_FOUND(404, "U001", "해당 사용자를 찾을 수 없습니다."),

    /**
     * 회원가입 시 이미 사용 중인 이메일로 요청한 경우
     * <p>HTTP 400 Bad Request</p>
     */
    EMAIL_DUPLICATION(400, "U002", "이미 존재하는 이메일입니다."),

    /**
     * 비활성화(탈퇴, 정지 등)된 계정으로 로그인을 시도한 경우
     * <p>HTTP 400 Bad Request (상황에 따라 403 Forbidden 사용 가능)</p>
     */
    USER_DISABLED(400, "U003", "중지된 계정입니다."),

    /**
     * 토큰 갱신 요청 시, DB에 해당 리프레시 토큰이 존재하지 않는 경우
     * <p>HTTP 401 Unauthorized</p>
     */
    REFRESH_TOKEN_NOT_FOUND(401, "U004", "리프레시 토큰을 찾을 수 없습니다."),

    /**
     * 리프레시 토큰의 유효 기간이 만료된 경우 (재로그인 필요)
     * <p>HTTP 401 Unauthorized</p>
     */
    REFRESH_TOKEN_EXPIRED(401, "U005", "리프레시 토큰이 만료되었습니다."),

    ;

    /**
     * HTTP 상태 코드 (예: 200, 400, 404, 500)
     */
    private final int status;

    /**
     * 애플리케이션 내부에서 사용하는 커스텀 에러 코드
     * <p>체계적인 에러 추적을 위해 정의합니다. (예: C001, U001)</p>
     */
    private final String code;

    /**
     * 클라이언트에게 전달할 사용자 친화적인 에러 메시지
     */
    private final String message;
}
