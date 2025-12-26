package com.github.sunguk0810.assignment.domain.auth.entity;

import com.github.sunguk0810.assignment.domain.auth.constant.RoleType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 사용자 또는 시스템의 접근 권한 정보를 관리하는 엔티티 클래스입니다.
 * <p>
 * {@link RoleType}과 매핑되어 권한의 유형, 이름, 설명을 정의하며
 * {@link BaseEntity}를 확장하여 데이터 생성/수정 이력을 관리합니다.
 * </p>
 *
 * @see RoleType
 */
@Entity
@Table(name = "roles", comment = "사용자 및 회사의 권한 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseEntity {
    /**
     * 권한 고유 식별자 (PK)
     * <p>데이터베이스 전략(Identity/Sequence)에 따라 자동 생성됩니다.</p>
     */
    @Id
    @Column(comment = "권한 ID (Sequence)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    /**
     * 권한 유형
     * <p>{@link RoleType} 열거형을 통해 관리됩니다 (예: {@code ROLE_USER}, {@code ROLE_ADMIN}).</p>
     */
    @Column(comment = "권한 유형", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    /**
     * 권한 표시 이름
     * <p>UI 등에서 사용자에게 노출될 권한의 이름입니다.</p>
     */
    @Column(comment = "권한 명", length = 50, nullable = false)
    private String roleName;

    /**
     * 권한에 대한 상세 설명
     */
    @Column(comment = "설명", length = 200)
    private String description;

    /**
     * Role 생성자 (Builder 패턴)
     *
     * @param roleType    권한 유형 ({@link RoleType})
     * @param roleName    권한 표시 이름
     * @param description 권한 설명
     */
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
