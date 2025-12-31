package com.github.sunguk0810.assignment.domain.health.controller;

import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSaveRequest;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.event.HealthMeasureProducer;
import com.github.sunguk0810.assignment.domain.health.service.HealthService;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/health/measure")
public class HealthMeasureController {
    private final HealthMeasureProducer producer;
    private final HealthService healthService;

    @PostMapping("")
    public ApiResponse<Void> saveMeasure(@RequestBody @Valid MeasureSaveRequest<? extends HealthDetail> request) {
        // 실제로는 토큰에서 유저 정보를 가져와서 recordKey를 검증하거나 세팅해야 할 수 있음.
        producer.saveLogAndProduce(request.getRecordKey(), request);
        return ApiResponse.success(null, "측정 데이터가 성공적으로 저장되었습니다.");
    }

    @GetMapping("")
    public ApiResponse<List<HealthSummaryResponse>> getMeasureInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid MeasureSummaryRequest request) {
        
        log.info("[getMeasureInfo] user={}, request={}", userDetails.getUsername(), request);
        return ApiResponse.success(
                healthService.getSummaries(userDetails.getUser().getRecordKey(), request),
                "건강 데이터 조회 성공"
        );
    }
}