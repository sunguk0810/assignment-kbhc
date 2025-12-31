package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthMeasureSummaryRepository
        extends JpaRepository<HealthMeasureSummary, Long>, HealthMeasureSummaryRepositoryCustom {
    Optional<HealthMeasureSummary> findByUserAndSummaryDateAndMeasureType(User user, LocalDate summaryDate, MeasureType measureType);

    List<HealthMeasureSummary> findByUserAndMeasureTypeAndSummaryDateBetween(User user, MeasureType measureType, LocalDate startDate, LocalDate endDate);
}
