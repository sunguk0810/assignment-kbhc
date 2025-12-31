package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BloodSugarSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("bloodSugar")
    private Long avgBloodSugar;

    public static BloodSugarSummaryResponse of(HealthMeasureSummary summary) {
        return BloodSugarSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgBloodSugar(summary.getAvgBloodSugar())
                .build();
    }
}