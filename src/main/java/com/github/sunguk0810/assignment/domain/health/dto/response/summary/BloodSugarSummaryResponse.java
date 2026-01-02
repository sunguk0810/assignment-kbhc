package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "혈당 요약 응답")
public class BloodSugarSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("bloodSugar")
    @Schema(description = "혈당")
    private Long avgBloodSugar;

    public static BloodSugarSummaryResponse of(HealthMeasureSummary summary) {
        return BloodSugarSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgBloodSugar(summary.getAvgBloodSugar())
                .build();
    }
}