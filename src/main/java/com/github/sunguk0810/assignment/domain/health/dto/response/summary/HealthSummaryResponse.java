package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "건강 측정 요약 응답")
public abstract class HealthSummaryResponse {
    @Setter
    @Schema(description = "요약일자", examples = {"2025-11-01", "2025-11"})
    private String summaryDate;

    @Schema(description = "측정 횟수")
    private Long count; // 측정 횟수
}
