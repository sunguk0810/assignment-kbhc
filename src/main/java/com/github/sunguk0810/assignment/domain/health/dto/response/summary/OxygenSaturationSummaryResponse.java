package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "산소포화도 요약 응답")
public class OxygenSaturationSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("oxygenSaturation")
    @Schema(description = "산소포화도", example = "97.5")
    private Double avgOxygenSaturation;

    public static OxygenSaturationSummaryResponse of(HealthMeasureSummary summary) {
        return OxygenSaturationSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgOxygenSaturation(summary.getAvgOxygenSaturation())
                .build();
    }
}