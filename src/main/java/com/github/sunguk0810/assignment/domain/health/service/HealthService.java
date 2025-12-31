package com.github.sunguk0810.assignment.domain.health.service;

import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;

import java.util.List;

public interface HealthService {
    List<HealthSummaryResponse> getSummaries(String userRecordKey, MeasureSummaryRequest request);

}
