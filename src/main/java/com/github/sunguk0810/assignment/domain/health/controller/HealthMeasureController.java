package com.github.sunguk0810.assignment.domain.health.controller;

import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSaveRequest;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.event.HealthMeasureProducer;
import com.github.sunguk0810.assignment.domain.health.service.HealthService;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 건강 측정 데이터 관리 및 조회를 위한 REST 컨트롤러입니다.
 * <p>
 * 사용자의 건강 측정 데이터를 수집하여 로그를 기록하고,
 * 일간/월간 요약 정보를 조회하는 API 엔드포인트를 제공합니다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/health/measure")
@Tag(name = "Health", description = "건강 데이터 API")
public class HealthMeasureController {
    private final HealthMeasureProducer producer;
    private final HealthService healthService;

    /**
     * 건강 측정 데이터를 저장하고 비동기 처리를 위한 이벤트를 발행합니다。
     * <p>
     * 클라이언트로부터 전달받은 측정 데이터 원본을 로그로 기록하며,
     * 실제 통계 및 요약 처리는 Kafka 이벤트를 통해 비동기적으로 수행됩니다.
     * </p>
     *
     * @param request 건강 측정 데이터 저장 요청 DTO (측정 타입, 데이터 항목, 사용자 키 등 포함)
     * @return 처리 성공 메시지를 포함한 {@link ApiResponse}
     */
    @PostMapping("")
    @Operation(summary = "건강 측정 정보 저장", description = "웨어러블 기기 등에서 수집된 건강 데이터를 업로드합니다. (비동기 처리)")
    public ApiResponse<Void> saveMeasure(@RequestBody @Valid MeasureSaveRequest<? extends HealthDetail> request) {
        producer.saveLogAndProduce(request.getRecordKey(), request);
        return ApiResponse.success(null, "측정 데이터가 성공적으로 저장되었습니다.");
    }

    /**
     * 사용자의 건강 측정 요약 정보를 조회합니다。
     * <p>
     * 인증된 사용자 정보를 기반으로 특정 기간 및 측정 타입에 대한
     * 일간(Daily) 또는 월간(Monthly) 요약 데이터를 반환합니다。
     * </p>
     *
     * @param userDetails 인증된 사용자 세부 정보 (Security Context)
     * @param request      조회 조건이 담긴 요청 DTO (측정 타입, 시작/종료일, 집계 방식)
     * @return 건강 요약 정보 리스트를 포함한 {@link ApiResponse}
     */
    @GetMapping("")
    @Operation(summary = "건강 데이터 조회", description = "기간별 건강 통계 데이터를 조회합니다.")
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