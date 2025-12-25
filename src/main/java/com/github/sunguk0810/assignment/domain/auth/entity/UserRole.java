package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "user_roles", comment = "사용자 권한 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole extends BaseEntity {
    @Id
    @Column(comment = "사용자 권한 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(
            name = "record_key",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_RECORD_KEY"),
            comment = "사용자 구분 키"
    )
    private User userInfo;

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_ROLE_ROLE_ID"),
            comment = "권한 ID"
    )
    private Role roleInfo;

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
