package com.github.sunguk0810.assignment.domain.company.entity;

import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "company_infos", comment = "회사 정보 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyInfo extends BaseEntity {
    @Id
    @Column(comment = "회사 상세정보 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyInfoId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, comment = "회사 ID")
    private Company company;

    @Column(comment = "회사명")
    private String name;

    @Column(comment = "회사 주소")
    private String address;

    @Column(comment = "회사 연락처")
    private String telNo;

    @Column(comment = "사업자등록번호")
    private String bizNo;

    @Builder
    public CompanyInfo(Company company, String name, String address, String telNo, String bizNo) {
        this.company = company;
        this.name = name;
        this.address = address;
        this.telNo = telNo;
        this.bizNo = bizNo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompanyInfo that = (CompanyInfo) o;
        return Objects.equals(companyInfoId, that.companyInfoId) && Objects.equals(company, that.company) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(telNo, that.telNo) && Objects.equals(bizNo, that.bizNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyInfoId, company, name, address, telNo, bizNo);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CompanyInfo.class.getSimpleName() + "[", "]")
                .add("companyInfoId=" + companyInfoId)
                .add("company=" + company)
                .add("name='" + name + "'")
                .add("address='" + address + "'")
                .add("telNo='" + telNo + "'")
                .add("bizNo='" + bizNo + "'")
                .toString();
    }
}
