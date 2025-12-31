package com.github.sunguk0810.assignment.domain.health.service;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSummaryRequest;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.HealthSummaryResponse;
import com.github.sunguk0810.assignment.domain.health.dto.response.summary.MeasureResponseMapper;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.domain.health.repository.HealthMeasureSummaryRepository;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 건강 데이터 조회 및 요약 관련 비즈니스 로직을 처리하는 서비스 구현체입니다.
 * <p>
 * 클라이언트의 요청에 따라 <b>일간(Daily)</b> 또는 <b>월간(Monthly)</b> 건강 요약 정보를 조회하고,
 * 이를 프레젠테이션 계층에 맞는 DTO로 변환하여 반환합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HealthServiceImpl implements HealthService {

    private final UserRepository userRepository;
    private final HealthMeasureSummaryRepository summaryRepository;

    /**
     * 특정 사용자의 건강 측정 요약 정보를 조회합니다.
     * <p>
     * 요청된 <b>집계 타입({@link SummaryType})</b>에 따라 적절한 리포지토리 메서드를 호출하여 데이터를 조회합니다.
     * </p>
     *
     * <h3>처리 로직</h3>
     * <ol>
     * <li>사용자 식별키(RecordKey)로 유효한 사용자인지 검증합니다.</li>
     * <li>{@code SummaryType}이 {@code DAILY}인 경우: 기간 내의 일자별 상세 데이터를 조회합니다.</li>
     * <li>{@code SummaryType}이 {@code MONTHLY}인 경우: 기간 내의 데이터를 월별로 그룹화하여 집계(SUM/AVG)합니다.</li>
     * <li>조회된 엔티티 리스트를 {@link MeasureResponseMapper}를 사용하여 응답 DTO로 변환합니다.</li>
     * </ol>
     *
     * @param userRecordKey 조회할 사용자의 고유 식별키 (UUID)
     * @param request       조회 조건이 담긴 요청 DTO (측정 타입, 기간, 집계 방식)
     * @return 조회된 건강 요약 정보 리스트 (다형성을 가진 {@link HealthSummaryResponse}의 하위 객체들)
     * @throws BusinessException 다음의 경우 발생:
     * <ul>
     * <li>{@link ErrorType#USER_NOT_FOUND}: 존재하지 않는 사용자인 경우</li>
     * <li>{@link ErrorType#INVALID_PARAMETER}: 지원하지 않는 집계 타입인 경우</li>
     * </ul>
     */
    @Override
    public List<HealthSummaryResponse> getSummaries(String userRecordKey, MeasureSummaryRequest request) {


        User user = userRepository.findById(userRecordKey)
                .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        MeasureType type = request.getType();
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        SummaryType summaryType = request.getSummaryType();

        List<HealthMeasureSummary> summaries;
        // 1. 기간별 데이터 조회
        if (SummaryType.DAILY.equals(summaryType)) {
            summaries = summaryRepository.findByUserAndMeasureTypeAndSummaryDateBetween(user, type, start, end);
        } else if (SummaryType.MONTHLY.equals(summaryType)) {
            summaries = summaryRepository.findMonthlyUserSummary(user, type, start, end);
        } else {
            throw new BusinessException(ErrorType.INVALID_PARAMETER);
        }

        return summaries.stream()
                .map(summary -> MeasureResponseMapper.mapToDto(summary, summaryType))
                .collect(Collectors.toList());
    }
}
