package com.github.sunguk0810.assignment.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 로그인 또는 토큰 갱신 성공 시 클라이언트에게 반환되는 인증 토큰 응답 DTO입니다.
 * <p>
 * OAuth 2.0 표준 형식을 따르며, 액세스 토큰과 리프레시 토큰, 그리고 만료 시간 정보를 포함합니다.
 * </p>
 */
@Getter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(description = "인증 토큰 응답 DTO")
public class AuthTokenResponse {
    /**
     * API 리소스 접근을 위한 액세스 토큰 (JWT)
     * <p>
     * HTTP Authorization 헤더에 담겨 전송됩니다.
     * <br>JSON 키: {@code "access_token"}
     * </p>
     */
    @JsonProperty("access_token")
    @Schema(description = "엑세스 토큰")
    private String accessToken;

    /**
     * 액세스 토큰 만료 시 재발급을 위해 사용하는 리프레시 토큰
     * <p>
     * 보안을 위해 액세스 토큰보다 긴 유효 기간을 가집니다.
     * <br>JSON 키: {@code "refresh_token"}
     * </p>
     */
    @JsonProperty("refresh_token")
    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    /**
     * 액세스 토큰의 남은 유효 기간
     * <p>
     * 단위: {@code 초(seconds)}
     * <br>JSON 키: {@code "expires_in"}
     * </p>
     */
    @JsonProperty("expires_in")
    @Schema(description = "토큰 유효시간 (초)")
    private Long expiresIn;

    /**
     * 토큰 인증 타입
     * <p>
     * 일반적인 OAuth 2.0 인증에서는 {@code "Bearer"} 타입을 사용합니다.
     * <br>JSON 키: {@code "token_type"}
     * </p>
     */
    @JsonProperty("token_type")
    @Schema(description = "토큰 인증 타입")
    private String tokenType;
}
