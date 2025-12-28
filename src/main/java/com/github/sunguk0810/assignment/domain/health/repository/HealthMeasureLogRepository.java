package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthMeasureLogRepository extends JpaRepository<HealthMeasureLog, Long> {
}
