package com.github.sunguk0810.assignment.domain.health.service;

import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HealthServiceTest {

    @Autowired
    private HealthService healthService;

    // 테스트용 정보
    private static final String TS_RECORD_KEY = "e27ba7ef-8bb2-424c-af1d-877e826b7487"; // admin2@admin.com
    private static final MeasureType TS_MEASURE_TYPE = MeasureType.STEPS;
    private static final LocalDate TS_START_DATE = LocalDate.of(2024, 11, 1);
    private static final LocalDate TS_END_DATE = LocalDate.of(2024, 12, 31);

    @Test
    @DisplayName("[TS-HLTH-01] 일간(Daily) 요약 조회")
    public void healthService_일간요약조회(){
        // given
        MeasureSummaryRequest request = MeasureSummaryRequest.builder()
                .type(TS_MEASURE_TYPE)
                .summaryType(SummaryType.DAILY)
                .startDate(TS_START_DATE)
                .endDate(TS_END_DATE)
                .build();

        // when
        List<HealthSummaryResponse> summaries = healthService.getSummaries(TS_RECORD_KEY, request);

        // then
        assertThat(summaries)
                .isNotEmpty()
                .first()
                .extracting(HealthSummaryResponse::getSummaryDate)
                .asString()
                .hasSize(10) // 2024-11-15
        ;
    }

    @Test
    @DisplayName("[TS-HLTH-02] 월간(Monthly) 요약 조회")
    public void healthService_월간요약조회(){
        // given
        MeasureSummaryRequest request = MeasureSummaryRequest.builder()
                .type(TS_MEASURE_TYPE)
                .summaryType(SummaryType.MONTHLY)
                .startDate(TS_START_DATE)
                .endDate(TS_END_DATE)
                .build();

        // when
        List<HealthSummaryResponse> summaries = healthService.getSummaries(TS_RECORD_KEY, request);

        // then
        assertThat(summaries)
                .isNotEmpty()
                .first()
                .extracting(HealthSummaryResponse::getSummaryDate)
                .asString()
                .hasSize(7) // 2024-11
        ;
    }
}