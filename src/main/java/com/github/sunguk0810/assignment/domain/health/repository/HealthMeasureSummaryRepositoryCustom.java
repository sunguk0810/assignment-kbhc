package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;

import java.time.LocalDate;
import java.util.List;

public interface HealthMeasureSummaryRepositoryCustom {
    List<HealthMeasureSummary> findMonthlyUserSummary(User user, MeasureType measureType, LocalDate startDate, LocalDate endDate);
}
