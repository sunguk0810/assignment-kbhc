package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "걸음 요약 응답")
public class StepSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("steps")
    @Schema(description = "걸음 수")
    private Long sumSteps;

    @JsonProperty("distance")
    @Schema(description = "거리")
    private Double sumDistance;

    @JsonProperty("calories")
    @Schema(description = "칼로리")
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