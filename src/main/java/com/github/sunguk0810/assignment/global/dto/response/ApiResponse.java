package com.github.sunguk0810.assignment.global.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 일관된 API 응답 포맷을 제공하는 래퍼(Wrapper) 클래스입니다.
 * <p>
 * 모든 API 응답은 이 클래스로 감싸져서 클라이언트에게 전달되며,
 * 성공 여부({@code status}), 메시지, 실제 데이터({@code data}), 에러 상세({@code error})를 포함합니다.
 * </p>
 *
 * @param <T> 실제 반환되는 데이터의 타입
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {
    /**
     * 응답 상태 (예: "success", "error", "fail")
     */
    private String status;

    /**
     * 응답 메시지 (사용자에게 보여줄 알림 또는 디버깅용 메시지)
     */
    private String message;

    /**
     * 성공 시 반환할 실제 데이터 (Payload)
     * <p>실패 시에는 {@code null}일 수 있습니다.</p>
     */
    private T data;

    /**
     * 실패 시 반환할 에러 상세 정보
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T error;

    /**
     * 응답 생성 일시
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 성공 응답을 생성합니다. (기본 메시지 사용)
     *
     * @param data 반환할 데이터
     * @param <T>  데이터 타입
     * @return 성공 상태의 ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "정상적으로 수행하였습니다.");
    }

    /**
     * 성공 응답을 생성합니다. (커스텀 메시지 사용)
     *
     * @param data    반환할 데이터
     * @param message 클라이언트에게 전달할 메시지
     * @param <T>     데이터 타입
     * @return 성공 상태의 ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 실패 응답을 생성합니다. (기본 메시지 사용)
     *
     * @param <T> 데이터 타입
     * @return 에러 상태의 ApiResponse
     */
    public static <T> ApiResponse<T> fail() {
        return fail("오류가 발생하였습니다.");
    }

    /**
     * 실패 응답을 생성합니다. (커스텀 메시지 사용)
     *
     * @param message 에러 메시지
     * @param <T>     데이터 타입
     * @return 에러 상태의 ApiResponse
     */
    public static <T> ApiResponse<T> fail(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .build();
    }

    /**
     * 유효성 검증 실패 등 상세 에러 정보를 포함한 실패 응답을 생성합니다.
     *
     * @param message 에러 메시지
     * @param errors  에러 상세 정보 (예: 필드별 에러 메시지 Map)
     * @param <T>     데이터 타입 (이 경우 보통 Void 혹은 호출 시점의 타입)
     * @return 에러 상태 및 상세 정보를 포함한 ApiResponse
     */
    public static ApiResponse<Map<String, List<String>>> fail(String message, Map<String, List<String>> errors) {
        return ApiResponse.<Map<String, List<String>>>builder()
                .status("error")
                .message(message)
                .error(errors)
                .data(null)
                .build();
    }



}
