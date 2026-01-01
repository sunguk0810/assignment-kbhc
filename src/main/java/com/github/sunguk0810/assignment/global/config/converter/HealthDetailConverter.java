package com.github.sunguk0810.assignment.global.config.converter;

import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * {@link HealthDetail} 객체와 데이터베이스 JSON 컬럼 간의 상호 변환을 담당하는 JPA 컨버터입니다.
 * <p>
 * 엔티티 저장 시에는 Java 객체를 JSON 문자열로 직렬화하고,
 * 데이터베이스 조회 시에는 저장된 JSON 문자열을 원래의 객체 타입으로 역직렬화합니다.
 * </p>
 */
@Converter
@Slf4j
public class HealthDetailConverter implements AttributeConverter<HealthDetail, String> {

    /**
     * JSON 직렬화/역직렬화를 위한 Jackson Mapper 인스턴스
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@link HealthDetail} 엔티티 속성을 DB 저장용 JSON 문자열로 변환합니다.
     *
     * @param healthDetail 변환할 건강 상세 정보 객체
     * @return 직렬화된 JSON 문자열
     * @throws RuntimeException JSON 직렬화 실패 시 발생
     */
    @Override
    public String convertToDatabaseColumn(HealthDetail healthDetail) {
        try {
            return objectMapper.writeValueAsString(healthDetail);
        } catch (JacksonException e) {
            log.error("Error converting HealthDetail to JSON. {}", e.getMessage());
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * DB에서 조회한 JSON 문자열을 {@link HealthDetail} 객체로 복원합니다.
     *
     * @param jsonString DB에서 조회된 JSON 문자열
     * @return 역직렬화된 {@link HealthDetail} 객체
     * @throws RuntimeException JSON 역직렬화 실패 시 발생
     */
    @Override
    public HealthDetail convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, HealthDetail.class);
        } catch (Exception e)  {
            log.error("Error converting json string to HealthDetail. {}", e.getMessage());
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
}
