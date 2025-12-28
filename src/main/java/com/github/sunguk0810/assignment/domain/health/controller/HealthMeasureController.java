package com.github.sunguk0810.assignment.domain.health.controller;

import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSaveRequest;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.event.HealthMeasureProducer;
import com.github.sunguk0810.assignment.global.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/health")
public class HealthMeasureController {
    private final HealthMeasureProducer producer;

    @PostMapping("measure")
    public ApiResponse<Void> saveMeasure(@RequestBody @Valid MeasureSaveRequest<? extends HealthDetail> request) {
        producer.saveLogAndProduce(request.getRecordKey(), request);
        return ApiResponse.success(null, "측정 데이터가 성공적으로 저장되었습니다.");
    }
}
