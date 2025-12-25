package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.domain.auth.constant.RoleType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "roles", comment = "사용자 및 회사의 권한 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {
    @Id
    @Column(comment = "권한 ID (Sequence)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(comment = "권한 유형", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(comment = "권한 명", length = 50, nullable = false)
    private String roleName;

    @Column(comment = "설명", length = 200)
    private String description;

    @Builder
    public Role(RoleType roleType, String roleName, String description) {
        this.roleType = roleType;
        this.roleName = roleName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role that = (Role) o;
        return Objects.equals(roleId, that.roleId) && roleType == that.roleType && Objects.equals(roleName, that.roleName) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleType, roleName, description);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]").add("roleId=" + roleId).add("roleType=" + roleType).add("roleName='" + roleName + "'").add("description='" + description + "'").toString();
    }


}
