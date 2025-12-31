package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StepSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("steps")
    private Long sumSteps;

    @JsonProperty("distance")
    private Double sumDistance;

    @JsonProperty("calories")
    private Double sumCalories;

    public static StepSummaryResponse of(HealthMeasureSummary summary) {
        return StepSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .sumSteps(summary.getSumSteps())
                .sumDistance(summary.getSumDistance())
                .sumCalories(summary.getSumCalories())
                .build();
    }
}