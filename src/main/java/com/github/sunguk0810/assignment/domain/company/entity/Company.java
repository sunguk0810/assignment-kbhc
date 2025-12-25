package com.github.sunguk0810.assignment.domain.company.entity;

import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "companies", comment = "회사 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {
    @Id
    @Column(comment = "회사 ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String companyId;

    @Column(comment = "계약시작일자")
    private LocalDate contractStartDate;

    @Column(comment = "계약종료일자")
    private LocalDate contractEndDate;

    @Column(comment = "활성화여부", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;




    @Builder
    public Company(LocalDate contractStartDate, LocalDate contractEndDate, Boolean isActive) {
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.isActive = isActive;
    }

    @Builder
    public Company(String companyId, LocalDate contractStartDate, LocalDate contractEndDate, Boolean isActive) {
        this.companyId = companyId;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(companyId, company.companyId) && Objects.equals(contractStartDate, company.contractStartDate) && Objects.equals(contractEndDate, company.contractEndDate) && Objects.equals(isActive, company.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, contractStartDate, contractEndDate, isActive);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Company.class.getSimpleName() + "[", "]")
                .add("companyId='" + companyId + "'")
                .add("contractStartDate=" + contractStartDate)
                .add("contractEndDate=" + contractEndDate)
                .add("isActive=" + isActive)
                .toString();
    }

    public void updateDate(LocalDate contractStartDate, LocalDate contractEndDate) {
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
    }
}
