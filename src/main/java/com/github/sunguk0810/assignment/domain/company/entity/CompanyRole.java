package com.github.sunguk0810.assignment.domain.company.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.Role;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "company_roles", comment = "회사 권한 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyRole extends BaseEntity {
    @Id
    @Column(comment = "회사 권한 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyRoleId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COMPANY_ROLE_COMPANY_ID"), comment = "회사 ID")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COMPANY_ROLE_ROLE_ID"), comment = "권한 ID")
    private Role role;

    @Builder
    public CompanyRole(Company company, Role role) {
        this.company = company;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompanyRole that = (CompanyRole) o;
        return Objects.equals(companyRoleId, that.companyRoleId) && Objects.equals(company, that.company) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyRoleId, company, role);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CompanyRole.class.getSimpleName() + "[", "]")
                .add("companyRoleId=" + companyRoleId)
                .add("company=" + company)
                .add("role=" + role)
                .toString();
    }
}
