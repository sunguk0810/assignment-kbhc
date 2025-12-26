package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 사용자와 권한(Role) 간의 매핑 정보를 관리하는 연결 엔티티(Associative Entity)입니다.
 * <p>
 * {@link User}와 {@link Role} 사이의 다대다 관계를 해소하며,
 * 특정 사용자에게 어떤 권한이 부여되었는지를 정의합니다.
 * </p>
 *
 * @see User
 * @see Role
 */
@Entity
@Table(name = "user_roles", comment = "사용자 권한 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole extends BaseEntity {
    /**
     * 사용자-권한 매핑 고유 ID (PK)
     * <p>데이터베이스 Identity 전략에 따라 자동 생성됩니다.</p>
     */
    @Id
    @Column(comment = "사용자 권한 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    /**
     * 권한을 부여받은 사용자 정보
     * <p>
     * {@link User} 엔티티와 다대일(N:1) 관계를 가집니다.
     * (FK: {@code record_key})
     * </p>
     */
    @ManyToOne
    @JoinColumn(
            name = "record_key",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_RECORD_KEY"),
            comment = "사용자 구분 키"
    )
    private User userInfo;

    /**
     * 사용자에게 부여된 권한 정보
     * <p>
     * {@link Role} 엔티티와 다대일(N:1) 관계를 가집니다.
     * (FK: {@code role_id})
     * </p>
     */
    @ManyToOne
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_ROLE_ID"),
            comment = "권한 ID"
    )
    private Role roleInfo;

    /**
     * UserRole 생성자 (Builder 패턴)
     *
     * @param userInfo 권한을 부여할 사용자 ({@link User})
     * @param roleInfo 부여할 권한 ({@link Role})
     */
    @Builder
    public UserRole(User userInfo, Role roleInfo) {
        this.userInfo = userInfo;
        this.roleInfo = roleInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(userRoleId, userRole.userRoleId) && Objects.equals(userInfo, userRole.userInfo) && Objects.equals(roleInfo, userRole.roleInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userRoleId, userInfo, roleInfo);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserRole.class.getSimpleName() + "[", "]")
                .add("userRoleId=" + userRoleId)
                .add("userInfo=" + userInfo)
                .add("roleInfo=" + roleInfo)
                .toString();
    }
}
