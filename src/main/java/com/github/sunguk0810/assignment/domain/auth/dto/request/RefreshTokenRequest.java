package com.github.sunguk0810.assignment.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 액세스 토큰 만료 시 재발급(Refresh)을 요청하기 위한 DTO 클래스입니다.
 * <p>
 * 클라이언트가 보유한 리프레시 토큰을 서버로 전송하여
 * 유효성을 검증받고 새로운 액세스 토큰을 발급받는 데 사용됩니다.
 * </p>
 */
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RefreshTokenRequest {
    /**
     * 리프레시 토큰 (Raw Token)
     * <p>
     * 필수 입력값이며, 빈 문자열이나 공백을 허용하지 않습니다.
     * </p>
     */
    @NotBlank(message = "refreshToken은 필수입니다.")
    private String refreshToken;
}
