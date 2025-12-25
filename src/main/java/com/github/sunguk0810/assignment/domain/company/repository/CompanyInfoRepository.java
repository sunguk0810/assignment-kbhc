package com.github.sunguk0810.assignment.domain.company.repository;

import com.github.sunguk0810.assignment.domain.company.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {
}
