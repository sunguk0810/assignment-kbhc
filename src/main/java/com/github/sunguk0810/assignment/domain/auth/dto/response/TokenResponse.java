package com.github.sunguk0810.assignment.domain.auth.dto.response;

import lombok.*;

/**
 * 토큰 생성 결과(토큰 값 및 유효 기간)를 담는 내부 전달용 DTO입니다.
 * <p>
 * {@link com.github.sunguk0810.assignment.global.config.security.TokenProvider}에서
 * 토큰 생성 후 컨트롤러나 서비스로 데이터를 전달할 때 사용됩니다.
 * </p>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenResponse {
    /**
     * 생성된 JWT 토큰 문자열
     * <p>액세스 토큰 또는 리프레시 토큰의 원문(Raw String)입니다.</p>
     */
    private String token;

    /**
     * 토큰의 유효 기간 (남은 시간)
     * <p>
     * {@code TokenProvider}의 생성 로직에 따라 단위가 결정됩니다.
     * 보통 {@code Date.getTime()} 연산 결과인 경우 <b>밀리초(ms)</b> 단위이며,
     * 클라이언트 응답(OAuth2 표준)으로 변환 시 <b>초(s)</b> 단위로 변환이 필요할 수 있습니다.
     * </p>
     */
    private Long expiresIn;
}
