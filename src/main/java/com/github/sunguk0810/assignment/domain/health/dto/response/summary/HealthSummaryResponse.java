package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class HealthSummaryResponse {
    @Setter
    private String summaryDate;
    private Long count; // 측정 횟수
}
