package com.github.sunguk0810.assignment.domain.auth.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 계정 정보({@link User})를 관리하는 데이터 접근 인터페이스입니다.
 * <p>
 * Spring Data JPA의 {@link JpaRepository}를 확장하여
 * 기본적인 CRUD(생성, 조회, 수정, 삭제) 및 페이징 기능을 제공합니다.
 * </p>
 *
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * 이메일 주소로 사용자 정보를 조회합니다.
     * <p>
     * 로그인 시 아이디(이메일)를 검증하거나, 사용자 정보를 가져올 때 사용합니다.
     * </p>
     *
     * @param email 조회할 사용자의 이메일 (Unique Key)
     * @return 해당 이메일을 가진 사용자 엔티티 (존재하지 않을 수 있음)
     */
    Optional<User> findByEmail(String email);


    /**
     * 사용자 고유 식별 키(recordKey)를 기준으로 사용자 정보를 조회합니다.
     *
     * @param recordKey 조회할 사용자의 고유 식별 키
     * @return 해당 recordKey를 가진 사용자 엔티티 (존재하지 않을 수 있음)
     */
    Optional<User> findByRecordKey(String recordKey);
    /**
     * 이메일 중복 여부를 확인합니다.
     * <p>
     * 회원가입 시 이미 가입된 이메일인지 검증할 때 사용합니다.
     * 전체 엔티티를 조회하는 {@code findByEmail}보다 성능상 유리합니다.
     * </p>
     *
     * @param email 중복을 검사할 이메일
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);
}
