package com.github.sunguk0810.assignment.domain.health.event;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.auth.repository.UserRepository;
import com.github.sunguk0810.assignment.domain.health.constant.DataSourceType;
import com.github.sunguk0810.assignment.domain.health.dto.device.DeviceInfo;
import com.github.sunguk0810.assignment.domain.health.dto.event.HealthMeasureEvent;
import com.github.sunguk0810.assignment.domain.health.dto.request.MeasureSaveRequest;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureLog;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.repository.HealthMeasureLogRepository;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import com.github.sunguk0810.assignment.global.constant.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

/**
 * 건강 측정 데이터의 저장 및 이벤트 발행을 담당하는 프로듀서(Producer) 서비스입니다.
 * <p>
 * 1. DB에 원본 로그(Raw Log) 저장<br>
 * 2. Kafka 토픽으로 이벤트 메시지 발행 (비동기 처리)<br>
 * 의 흐름을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthMeasureProducer {
    private final HealthMeasureLogRepository logRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 건강 데이터 측정 이벤트가 발행될 Kafka 토픽 이름
     */
    private static final String TOPIC = "health-measure-topic";

    /**
     * 측정 데이터를 DB에 저장하고, 후속 처리를 위해 Kafka 이벤트를 발행합니다.
     *
     * @param recordKey 사용자 식별 키 (Kafka 파티셔닝 키로 사용됨)
     * @param request   저장할 측정 데이터 요청 객체
     * @throws BusinessException JSON 직렬화 실패 시 발생 (Internal Server Error)
     */
    @Transactional
    public void saveLogAndProduce(String recordKey, MeasureSaveRequest<? extends HealthDetail> request){
        try  {
            // recordKey가 users 테이블에 존재하는지 체크한다.


            String rawJson = objectMapper.writeValueAsString(request);

            MeasureSaveRequest.Data<? extends HealthDetail> data = request.getData();

            User userInfo = User.builder()
                    .recordKey(recordKey)
                    .build();

            DeviceInfo deviceInfo = data.getSource();

            DataSourceType dataSourceType = DataSourceType.from(deviceInfo.getName());

            HealthMeasureLog measureLog = HealthMeasureLog.builder()
                    .userInfo(userInfo)
                    .rawJson(rawJson)
                    .dataSourceType(dataSourceType)
                    .status(StatusType.PENDING)
                    .build();

            logRepository.save(measureLog);

            HealthMeasureEvent event = HealthMeasureEvent.builder()
                    .measureLogId(measureLog.getMeasureLogId())
                    .recordKey(recordKey)
                    .build();

            sendToKafka(recordKey, event);

            log.info("HealthMeasureEvent sent to kafka topic: {}", TOPIC);
        } catch (JacksonException e) {
            throw new BusinessException(ErrorType.INVALID_JSON_FORMAT);
        }
    }

    private void sendToKafka(String key, HealthMeasureEvent event){

        try {
            String jsonMessage = objectMapper.writeValueAsString(event);
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, key, jsonMessage);
            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    log.error("Failed to send message={}", event, exception);
                } else {
                    log.info("Sent message={}", event);
                }
            });
        } catch (JacksonException e) {
            log.error("Failed to serialize event={}", event, e);
            throw new BusinessException(ErrorType.INVALID_JSON_FORMAT);
        }

    }

}
