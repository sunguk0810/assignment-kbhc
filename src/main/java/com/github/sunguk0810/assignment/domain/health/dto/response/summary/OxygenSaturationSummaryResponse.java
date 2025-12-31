package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class OxygenSaturationSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("oxygenSaturation")
    private Double avgOxygenSaturation;

    public static OxygenSaturationSummaryResponse of(HealthMeasureSummary summary) {
        return OxygenSaturationSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgOxygenSaturation(summary.getAvgOxygenSaturation())
                .build();
    }
}