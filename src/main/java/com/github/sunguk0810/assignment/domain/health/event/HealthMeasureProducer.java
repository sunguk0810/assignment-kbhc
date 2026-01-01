package com.github.sunguk0810.assignment.domain.health.event;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
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
 * 건강 측정 데이터의 저장 및 이벤트 발행을 담당하는 프로듀서(Producer) 서비스 클래스입니다.
 * <p>
 * 클라이언트로부터 수신된 건강 측정 원본 데이터를 DB에 로그로 기록하고,
 * 비동기 처리를 위해 Kafka 토픽({@code health-measure-topic})으로 이벤트를 발행합니다.
 * </p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *     <li>원본 측정 데이터(JSON)를 {@link HealthMeasureLog} 엔티티로 변환하여 DB 저장</li>
 *     <li>데이터 분석 및 요약 처리를 위한 {@link HealthMeasureEvent} 발행</li>
 *     <li>Kafka 메시지 발행 시 사용자 식별키({@code recordKey})를 메시지 키로 사용하여 순서 보장</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HealthMeasureProducer {
    private final HealthMeasureLogRepository logRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 건강 데이터 측정 이벤트가 발행될 Kafka 토픽 이름
     */
    private static final String TOPIC = "health-measure-topic";

    /**
     * 측정 데이터를 로그로 저장하고 Kafka 이벤트를 발행합니다.
     * <p>
     * 수신된 요청 데이터를 JSON 문자열로 변환하여 DB에 저장한 후,
     * 저장이 완료되면 생성된 로그 ID를 포함한 이벤트를 Kafka로 전송합니다.
     * </p>
     *
     * <h3>처리 흐름</h3>
     * <ol>
     *     <li>요청 객체({@link MeasureSaveRequest})를 JSON 문자열로 직렬화</li>
     *     <li>데이터 소스 정보(디바이스 등) 추출 및 분석</li>
     *     <li>{@link HealthMeasureLog} 엔티티 생성 및 DB 저장 (상태: {@code PENDING})</li>
     *     <li>저장된 로그 정보를 바탕으로 {@link HealthMeasureEvent} 생성</li>
     *     <li>{@link #sendToKafka} 메서드를 호출하여 메시지 발행</li>
     * </ol>
     *
     * @param recordKey 사용자 식별 키 (Kafka 파티셔닝 키로 사용됨)
     * @param request   저장할 측정 데이터 요청 객체
     * @throws BusinessException {@link ErrorType#INVALID_JSON_FORMAT} JSON 직렬화 실패 시 발생
     */
    @Transactional
    public void saveLogAndProduce(String recordKey, MeasureSaveRequest<? extends HealthDetail> request){
        try  {
            // recordKey가 users 테이블 e존재하는지 체크한다.


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

    /**
     * Kafka 토픽으로 이벤트를 비동기적으로 전송합니다.
     * <p>
     * {@link KafkaTemplate}을 사용하여 메시지를 전송하며, 전송 결과(성공/실패)를 로그로 기록합니다.
     * </p>
     *
     * @param key   Kafka 메시지의 키 (동일 사용자의 데이터 순서 보장을 위해 사용)
     * @param event 발행할 건강 측정 이벤트 객체
     * @throws BusinessException JSON 직렬화 실패 시 발생
     */
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
