package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HeartRateSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("heartRate")
    private Long avgHeartRate;

    public static HeartRateSummaryResponse of(HealthMeasureSummary summary) {
        return HeartRateSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgHeartRate(summary.getAvgHeartRate())
                .build();
    }
}