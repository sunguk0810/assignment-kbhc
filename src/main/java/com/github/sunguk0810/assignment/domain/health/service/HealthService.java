package com.github.sunguk0810.assignment.domain.health.service;

import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;

import java.util.List;

/**
 * 건강 측정 데이터 조회 및 요약 관련 비즈니스 로직을 정의하는 인터페이스입니다.
 * <p>
 * 사용자의 건강 상태를 파악하기 위한 다양한 요약 정보를 제공하는 기능을 명세합니다.
 * </p>
 */
public interface HealthService {

    /**
     * 특정 사용자의 건강 측정 요약 정보를 조회합니다.
     * <p>
     * 요청된 조건(기간, 측정 유형, 집계 방식)에 따라 일간 또는 월간 단위의
     * 요약된 건강 데이터를 반환합니다.
     * </p>
     *
     * @param recordKey 조회할 사용자의 고유 식별키 (UUID)
     * @param request       조회 조건이 담긴 요청 DTO (측정 타입, 기간, 집계 방식 등)
     * @return 조회된 건강 요약 정보 리스트 ({@link HealthSummaryResponse}의 구현체 목록)
     */
    List<HealthSummaryResponse> getSummaries(String recordKey, MeasureSummaryRequest request);

}
