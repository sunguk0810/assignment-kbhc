package com.github.sunguk0810.assignment.domain.auth.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.UserProfile;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 프로필 정보({@link UserProfile})를 관리하는 데이터 접근 인터페이스입니다.
 * <p>
 * Spring Data JPA의 {@link JpaRepository}를 확장하여 기본적인 CRUD 및 페이징/정렬 기능을 제공합니다.
 * </p>
 *
 * @see UserProfile
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * 주어진 recordKey를 기준으로 사용자 프로필 정보를 조회합니다.
     *
     * @param recordKey 사용자 식별을 위한 고유 키
     * @return recordKey와 매칭되는 사용자 프로필 정보가 포함된 Optional 객체. 해당 데이터가 없을 경우 빈 Optional을 반환.
     */
    @Query("""
    SELECT p
      FROM UserProfile p
     WHERE p.user.recordKey = :recordKey
    """)
    Optional<UserProfile> findByRecordKey(@Param("recordKey") String recordKey);
}
