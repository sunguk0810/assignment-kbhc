package com.github.sunguk0810.assignment.domain.company.repository;

import com.github.sunguk0810.assignment.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

}
