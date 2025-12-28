package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.domain.auth.constant.RoleType;
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
@Table(name = "users",
        comment = "사용자 테이블",
        uniqueConstraints = {
         @UniqueConstraint(columnNames = {"email"}, name = "UK_USER_EMAIL")
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
    @Column(comment = "사용자 구분키", name = "record_key")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordKey;

    /**
     * 사용자 이메일 (로그인 ID)
     * <p>
     * 시스템 내에서 고유해야 하며, {@code UK_USER_EMAIL} 유니크 제약조건이 적용되어 중복 저장이 불가능합니다.
     * </p>
     */
    @Column(comment = "이메일", nullable = false)
    private String email;

    /**
     * 사용자 이름 (Display Name)
     * <p>최대 33자까지 허용됩니다.</p>
     */
    @Column(comment = "이름", length = 100, nullable = false)
    private String username;

    /**
     * 암호화된 비밀번호
     * <p>평문(Plain Text)이 아닌 해시(Hash) 처리된 문자열이 저장됩니다.</p>
     */
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

    /**
     * 마지막 로그인 일시
     * <p>로그인 성공 시마다 갱신됩니다.</p>
     */
    @Column(comment = "마지막로그인일자")
    private LocalDateTime lastLoginAt;

    @Column(nullable = false, comment = "권한 타입")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;


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
    public User(String recordKey, String email, String username, String hashedPassword, RoleType roleType, Boolean isActive) {
        this.recordKey = recordKey;
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.isActive = isActive;
        this.roleType = roleType;
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 사용자 프로필을 추가합니다.
     *
     * 전달된 {@link UserProfile} 객체와 현재 사용자 간의 연관관계를 설정하며,
     * 사용자 객체의 {@code userProfile} 필드를 갱신합니다.
     *
     * @param profile 추가할 사용자 프로필 객체
     */
    public void addProfile(UserProfile profile){
        profile.addUser(this);
        this.userProfile = profile;
    }

    /**
     * 마지막 로그인 시간을 현재 시간으로 갱신합니다.
     *
     * @return 갱신된 마지막 로그인 시간
     */
    public LocalDateTime updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
        return this.lastLoginAt;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(recordKey, user.recordKey) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(hashedPassword, user.hashedPassword) && Objects.equals(isActive, user.isActive) && Objects.equals(lastLoginAt, user.lastLoginAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, email, username, hashedPassword, isActive, lastLoginAt);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("recordKey='" + recordKey + "'")
                .add("email='" + email + "'")
                .add("username='" + username + "'")
                .add("hashedPassword='" + hashedPassword + "'")
                .add("isActive=" + isActive)
                .add("lastLoginAt=" + lastLoginAt)
                .toString();
    }

}
