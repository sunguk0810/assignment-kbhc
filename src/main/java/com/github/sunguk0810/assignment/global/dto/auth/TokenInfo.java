package com.github.sunguk0810.assignment.global.dto.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Redis에 저장되는 리프레시 토큰 엔티티 클래스입니다.
 * <p>
 * 사용자 인증 갱신을 위한 리프레시 토큰 정보를 저장하며,
 * 설정된 TTL(Time To Live)이 지나면 Redis에서 자동으로 삭제됩니다.
 * </p>
 *
 * @see RedisHash
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash(value = "refresh_token", timeToLive = 1209600) // 1209600초 = 14일
public class TokenInfo {
    /**
     * Redis Key 식별자 (PK)
     * <p>
     * 실제 Redis에는 {@code "refresh_token:{UUID}"} 형태의 Key로 저장됩니다.
     * </p>
     */
    @Id
    private String id;

    /**
     * 사용자 식별 키 (User Record Key)
     * <p>어떤 사용자의 토큰인지 구별하기 위해 저장합니다.</p>
     */
    @Indexed
    private String recordKey;

    /**
     * 리프레시 토큰 값 (Indexed)
     * <p>
     * {@link Indexed} 어노테이션을 통해 보조 인덱스(Secondary Index)가 생성됩니다.
     * 이를 통해 ID가 아닌 토큰 값으로도 데이터를 조회({@code findByRefreshToken})할 수 있습니다.
     * </p>
     */
    @Indexed
    private String refreshToken;

    /**
     * 토큰 만료 일시 (논리적 만료)
     * <p>
     * Redis의 TTL에 의한 물리적 삭제와 별개로, 애플리케이션 로직에서
     * 토큰의 유효성을 2차 검증하기 위해 사용합니다.
     * </p>
     */
    private LocalDateTime expiryDate;
}
