package com.github.sunguk0810.assignment.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 로그아웃 요청 시 클라이언트로부터 전달받는 DTO 클래스입니다.
 * <p>
 * 서버(Redis 등)에 저장된 리프레시 토큰을 삭제하여
 * 더 이상 해당 토큰으로 액세스 토큰을 재발급받지 못하도록 만들기 위해 사용됩니다.
 * </p>
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "로그아웃 요청 DTO")
public class LogoutRequest {
    /**
     * 삭제(무효화)할 리프레시 토큰 값
     * <p>
     * 로그아웃 시 클라이언트는 가지고 있던 리프레시 토큰을 서버로 보내야 하며,
     * 서버는 이 토큰을 저장소에서 찾아 삭제합니다.
     * </p>
     */
    @NotBlank(message = "refreshToken은 필수입니다.")
    @Schema(description = "리프레시 토큰")
    private String refreshToken;
}
