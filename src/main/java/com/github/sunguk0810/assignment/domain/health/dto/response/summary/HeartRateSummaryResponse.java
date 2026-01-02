package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "심박수 요약 응답")
public class HeartRateSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("heartRate")
    @Schema(description = "심박수")
    private Long avgHeartRate;

    public static HeartRateSummaryResponse of(HealthMeasureSummary summary) {
        return HeartRateSummaryResponse.builder()
                .summaryDate(summary.getSummaryDate().toString())
                .count(summary.getCount())
                .avgHeartRate(summary.getAvgHeartRate())
                .build();
    }
}