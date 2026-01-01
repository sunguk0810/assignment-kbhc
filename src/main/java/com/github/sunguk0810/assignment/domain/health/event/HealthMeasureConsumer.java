package com.github.sunguk0810.assignment.domain.health.event;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.dto.event.HealthMeasureEvent;
import com.github.sunguk0810.assignment.domain.health.dto.measure.*;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSaveRequest;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureInfo;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureLog;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import com.github.sunguk0810.assignment.domain.health.repository.HealthMeasureInfoRepository;
import com.github.sunguk0810.assignment.domain.health.repository.HealthMeasureLogRepository;
import com.github.sunguk0810.assignment.domain.health.repository.HealthMeasureSummaryRepository;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.constant.StatusType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

/**
 * 건강 측정 데이터를 Kafka에서 소비하여 처리하는 소비자 서비스 클래스입니다.
 * <p>
 * Kafka 토픽({@code health-measure-topic})으로부터 건강 측정 이벤트 메시지를 수신하고,
 * 이를 파싱하여 개별 측정 정보를 저장하고 일일 요약 정보를 업데이트하는 역할을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthMeasureConsumer {
    private final HealthMeasureLogRepository logRepository;
    private final HealthMeasureSummaryRepository summaryRepository;
    private final HealthMeasureInfoRepository infoRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    /**
     * Kafka 토픽에서 건강 측정 이벤트를 수신하여 처리합니다.
     * <p>
     * 수신된 JSON 문자열을 {@link HealthMeasureEvent}로 변환한 후, 관련 로그를 조회하여
     * 실제 측정 데이터({@code rawJson})를 비즈니스 로직에 따라 처리합니다.
     * </p>
     *
     * <h3>처리 단계</h3>
     * <ol>
     * <li>이벤트 메시지(JSON) 역직렬화</li>
     * <li>측정 로그({@link HealthMeasureLog}) 조회</li>
     * <li>원본 데이터 역직렬화 및 사용자 검증</li>
     * <li>데이터 항목별 저장 및 요약 정보 갱신 ({@link #processEntries})</li>
     * <li>처리 결과에 따른 로그 상태 업데이트 ({@code DONE} 또는 {@code ERROR})</li>
     * </ol>
     *
     * @param eventString Kafka로부터 수신된 JSON 형식의 이벤트 메시지
     */
    @KafkaListener(topics = "health-measure-topic", groupId = "health-measure-group")
    @Transactional
    public void consume(String eventString) {
        log.info("Event consumed : {}", eventString);

        HealthMeasureEvent event = objectMapper.readValue(eventString, HealthMeasureEvent.class);

        HealthMeasureLog measureLog = logRepository.findById(event.getMeasureLogId())
                .orElseThrow(() -> new BusinessException(ErrorType.LOG_NOT_FOUND));

        try {
            MeasureSaveRequest<? extends HealthDetail> request = objectMapper.readValue(measureLog.getRawJson(), new TypeReference<>() {
            });
            User user = userRepository.findByRecordKey(event.getRecordKey())
                    .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

            processEntries(user, request, measureLog);
            measureLog.updateStatus(StatusType.DONE);
            logRepository.save(measureLog);


            log.info("FINISH JOB Event ID = {}", event.getMeasureLogId());
        } catch (Exception e) {
            log.error("Error occurred while processing measure log : {}, message : {}", measureLog, e.getMessage());
            measureLog.updateStatus(StatusType.ERROR);
            logRepository.save(measureLog);
        }

    }

    /**
     * 측정 데이터 내의 개별 항목들을 순회하며 DB에 저장하고 요약 정보를 업데이트합니다.
     *
     * @param user       측정 데이터를 소유한 사용자 정보 ({@link User})
     * @param request    저장 요청된 건강 측정 데이터 요청 객체
     * @param measureLog 현재 처리 중인 측정 로그 엔티티
     * @throws BusinessException {@link ErrorType#INVALID_JSON_FORMAT} JSON 변환 실패 시 발생
     */
    private void processEntries(User user, MeasureSaveRequest<? extends HealthDetail> request, HealthMeasureLog measureLog) {
        if (request.getData() == null || request.getData().getEntries() == null) return;

        Long measureLogId = measureLog.getMeasureLogId();
        for (HealthDetail entry : request.getData().getEntries()) {

            LocalDate measureDate = entry.getPeriod().getFrom().toLocalDate();
            HealthMeasureInfo info = HealthMeasureInfo.builder()
                    .measureLogId(measureLogId)
                    .measureType(request.getType())
                    .period(entry.getPeriod())
                    .userInfo(user)
                    .build();

            try {
                String detailJson = objectMapper.writeValueAsString(entry);
                int affectedRows = infoRepository.saveInfo(info, detailJson);
                if (affectedRows > 0) {
                    updateSummary(user, measureDate, request.getType(), entry);
                }
            } catch (JacksonException e) {
                throw new BusinessException(ErrorType.INVALID_JSON_FORMAT);
            }
        }
    }

    /**
     * 사용자의 일일 건강 측정 요약 정보({@link HealthMeasureSummary})를 업데이트하거나 새로 생성합니다.
     * <p>
     * 특정 날짜와 측정 유형에 해당하는 요약 정보가 존재하면 기존 정보를 업데이트하고,
     * 존재하지 않으면 새로운 요약 엔티티를 생성하여 저장합니다.
     * </p>
     *
     * @param user   사용자 정보
     * @param date   측정 날짜
     * @param type   측정 유형 ({@link MeasureType})
     * @param detail 측정 상세 데이터 ({@link HealthDetail})
     */
    private void updateSummary(User user, LocalDate date, MeasureType type, HealthDetail detail) {
        HealthMeasureSummary summary = summaryRepository
                .findByUserAndSummaryDateAndMeasureType(user, date, type)
                .orElseGet(() -> HealthMeasureSummary.builder()
                        .user(user)
                        .summaryDate(date)
                        .measureType(type)
                        .sumSteps(0L)
                        .sumCalories(0.0)
                        .sumDistance(0.0)
                        .avgDiastolic(0L)
                        .avgBloodSugar(0L)
                        .avgHeartRate(0L)
                        .avgOxygenSaturation(0.0)
                        .count(0L)
                        .build());

        detail.applyTo(summary);
        summaryRepository.save(summary);
    }

}
