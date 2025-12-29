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

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthMeasureConsumer {
    private final HealthMeasureLogRepository logRepository;
    private final HealthMeasureSummaryRepository summaryRepository;
    private final HealthMeasureInfoRepository infoRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "health-measure-topic", groupId = "health-measure-group")
    @Transactional
    public void consume(String eventString) {
        log.info("Event consumed : {}", eventString);

        HealthMeasureEvent event = objectMapper.readValue(eventString, HealthMeasureEvent.class);

        log.info("event = {}", event);

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

    private void updateSummary(User user, LocalDate date, MeasureType type, HealthDetail detail) {
        HealthMeasureSummary summary = summaryRepository
                .findByUserAndSummaryDateAndMeasureType(user, date, type)
                .orElseGet(() -> HealthMeasureSummary.builder()
                        .user(user)
                        .summaryDate(date)
                        .measureType(type)
                        .sumValue(0.0)
                        .avgValue(0.0)
                        .count(0)
                        .minValue(0.0)
                        .maxValue(0.0)
                        .build());

        double value = extractValueFromDetail(detail);

        boolean isAccumulate = (type == MeasureType.STEPS);

        summary.updateSummary(value, isAccumulate);

        summaryRepository.save(summary);
    }

    private double extractValueFromDetail(HealthDetail detail) {
        if (detail instanceof Step step) {
            return step.getSteps();
        } else if (detail instanceof BloodPressure bp) {
            // 혈압은 수축기 혈압을 사용
            return getValueFromQuantity(bp.getSystolic());
        } else if (detail instanceof HeartRate hr) {
            return getValueFromQuantity(hr.getHeartRate());
        } else if (detail instanceof OxygenSaturation os) {
            return getValueFromQuantity(os.getOxygenSaturation());
        } else if (detail instanceof BloodSugar bs) {
            return getValueFromQuantity(bs.getBloodSugar());
        }
        log.warn("Unknown detail type : {}", detail.getClass());
        return 0.0;
    }

    private double getValueFromQuantity(Quantity quantity) {
        if (quantity != null && quantity.getValue() != null) return quantity.getValue();
        return 0.0;
    }
}
