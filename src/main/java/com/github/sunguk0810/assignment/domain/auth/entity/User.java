package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
//https://stir.tistory.com/158

/**
 * 사용자 계정 및 프로필 정보를 관리하는 엔티티 클래스입니다.
 * <p>
 * 이메일을 고유 식별자(Login ID)로 사용하며, {@link BaseEntity}를 확장하여
 * 데이터의 생명주기(생성/수정)를 관리합니다.
 * </p>
 *
 * @see BaseEntity
 */
@Entity
@Table(name = "user",
        comment = "사용자 테이블",
        uniqueConstraints = {
         @UniqueConstraint(columnNames = {"email"}, name = "UK_USER_EMAIL"),
         @UniqueConstraint(columnNames = {"company_id"}, name = "UK_USER_COMPANY_ID")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    /**
     * 사용자 고유 식별 키 (PK)
     * <p>
     * UUID 전략을 사용하여 자동으로 생성됩니다.
     * </p>
     */
    @Id
    @Column(comment = "사용자 구분키")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordKey;

    @Column(comment = "이메일", nullable = false)
    private String email;

    /**
     * 사용자 이름 (Display Name)
     * <p>최대 33자까지 허용됩니다.</p>
     */
    @Column(comment = "이름", length = 100, nullable = false)
    private String username;

    @Column(comment = "비밀번호", nullable = false)
    private String hashedPassword;

    /**
     * 계정 활성화 여부
     * <p>
     * 기본값은 {@code true}이며, {@code false}인 경우 로그인이 제한될 수 있습니다.
     * </p>
     */
    @Column(comment = "활성화여부", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(comment = "마지막로그인일자")
    private LocalDateTime lastLoginAt;

    /**
     * User 생성자 (Builder 패턴)
     * <p>
     * 객체 생성 시 {@code lastLoginAt}은 현재 시간({@link LocalDateTime#now()})으로 자동 초기화됩니다.
     * </p>
     *
     * @param email          사용자 이메일
     * @param username       사용자 이름
     * @param hashedPassword 암호화된 비밀번호
     * @param isActive       활성화 여부
     */
    @Builder
    public User(String email, String username, String hashedPassword, Company company, Boolean isActive) {
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.company = company;
        this.isActive = isActive;
        this.lastLoginAt = LocalDateTime.now();
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(recordKey, user.recordKey) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(company, user.company) && Objects.equals(isActive, user.isActive) && Objects.equals(lastLoginAt, user.lastLoginAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, email, username, hashedPassword, company, isActive, lastLoginAt);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("recordKey='" + recordKey + "'")
                .add("email='" + email + "'")
                .add("username='" + username + "'")
                .add("hashedPassword='" + hashedPassword + "'")
                .add("company=" + company)
                .add("isActive=" + isActive)
                .add("lastLoginAt=" + lastLoginAt)
                .toString();
    }

}
