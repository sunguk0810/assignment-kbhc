package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BloodPressureSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("systolic")
    private Long avgSystolic;

    @JsonProperty("diastolic")
    private Long avgDiastolic;

    public static BloodPressureSummaryResponse of(HealthMeasureSummary summary) {
        return BloodPressureSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgSystolic(summary.getAvgSystolic())
                .avgDiastolic(summary.getAvgDiastolic())
                .build();
    }
}