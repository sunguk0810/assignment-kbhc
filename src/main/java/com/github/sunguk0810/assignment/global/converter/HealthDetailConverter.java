package com.github.sunguk0810.assignment.global.converter;

import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * {@link HealthDetail} 객체와 데이터베이스의 JSON 컬럼 간 변환을 담당하는 JPA 컨버터입니다.
 * <p>
 * 엔티티가 데이터베이스에 저장될 때는 객체를 JSON 문자열로 직렬화하고,
 * 데이터베이스에서 조회될 때는 JSON 문자열을 다시 객체로 역직렬화합니다.
 * </p>
 *
 * @see HealthDetail
 * @see com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureInfo
 */
@Converter
@Slf4j
public class HealthDetailConverter implements AttributeConverter<HealthDetail, String> {
    /**
     * JSON 처리를 위한 Jackson ObjectMapper 인스턴스
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 엔티티의 {@link HealthDetail} 속성을 데이터베이스 컬럼(JSON 문자열)으로 변환합니다.
     *
     * @param healthDetail 변환할 건강 측정 상세 객체
     * @return DB에 저장될 JSON 포맷의 문자열
     * @throws RuntimeException JSON 직렬화 실패 시 발생
     */
    @Override
    public String convertToDatabaseColumn(HealthDetail healthDetail) {
        try {
            // 건강 측정 상세의 객체를 JSON 문자열로 변경한다.
            return objectMapper.writeValueAsString(healthDetail);
        } catch (JacksonException e) {
            log.error("Error converting HealthDetail to JSON. {}", e.getMessage());
            throw new RuntimeException("JSON serialization failed", e);
        }
    }
    /**
     * 데이터베이스의 컬럼(JSON 문자열)을 엔티티의 {@link HealthDetail} 속성으로 변환합니다.
     *
     * @param jsonString DB에서 조회한 JSON 포맷의 문자열
     * @return 복원된 {@link HealthDetail} 객체
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
