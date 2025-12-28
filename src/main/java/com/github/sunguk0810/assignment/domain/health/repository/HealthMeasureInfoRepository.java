package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthMeasureInfoRepository extends JpaRepository<HealthMeasureInfo, Long> {
}
