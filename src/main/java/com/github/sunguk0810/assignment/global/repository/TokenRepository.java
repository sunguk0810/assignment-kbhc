package com.github.sunguk0810.assignment.global.repository;

import com.github.sunguk0810.assignment.global.dto.auth.TokenInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Redis에 저장된 리프레시 토큰({@link TokenInfo})을 관리하는 데이터 접근 인터페이스입니다.
 * <p>
 * Spring Data Redis의 {@link CrudRepository}를 상속받아 기본적인 CRUD 기능을 제공하며,
 * 메서드 쿼리(Query Method)를 통해 보조 인덱스(Secondary Index) 기반의 조회를 수행합니다.
 * </p>
 *
 * @see TokenInfo
 */
@Repository
public interface TokenRepository extends CrudRepository<TokenInfo, String> {
    /**
     * 리프레시 토큰 값(Raw Token)으로 토큰 정보를 조회합니다.
     * <p>
     * 토큰 재발급(Refresh) 요청 시, 클라이언트가 보낸 토큰이 유효한지 확인할 때 사용합니다.
     * {@link TokenInfo#getRefreshToken()} 필드에 {@code @Indexed}가 설정되어 있어야 작동합니다.
     * </p>
     *
     * @param refreshToken 조회할 리프레시 토큰 문자열
     * @return 토큰 정보가 담긴 Optional 객체
     */
    Optional<TokenInfo> findByRefreshToken(String refreshToken);

    /**
     * 사용자 식별 키(Record Key)로 토큰 정보를 조회합니다.
     * <p>
     * 특정 사용자의 토큰을 만료시키거나(로그아웃, 강제 종료), 중복 로그인을 체크할 때 사용할 수 있습니다.
     * <b>주의:</b> 이 메서드가 작동하려면 {@link TokenInfo#getRecordKey()} 필드에도 {@code @Indexed}가 붙어 있어야 합니다.
     * </p>
     *
     * @param recordKey 사용자 고유 식별 키
     * @return 해당 사용자의 토큰 정보가 담긴 Optional 객체
     */
    Optional<TokenInfo> findByRecordKey(String recordKey);
}
