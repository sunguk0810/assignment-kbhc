package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "혈압 요약 응답")
public class BloodPressureSummaryResponse extends HealthSummaryResponse {
    @JsonProperty("systolic")
    @Schema(description = "수축기 혈압")
    private Long avgSystolic;

    @JsonProperty("diastolic")
    @Schema(description = "이완기 혈압")
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