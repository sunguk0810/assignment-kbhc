package com.github.sunguk0810.assignment.global.config.serializer;


import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class MobileDateSerializer extends ValueDeserializer<LocalDateTime> {

    // Android: "2024-11-16 22:20:00"
    private static final DateTimeFormatter ANDROID_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Apple: "2024-12-15T12:50:00+0000"
    private static final DateTimeFormatter APPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    // Default
    private static final DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws JacksonException {

        String dateString = jsonParser.getString();
        if (dateString == null || dateString.isEmpty()){
            return null;
        }

        try {
            // T가 포함되어 있다면 Apple로 가정
            if (dateString.contains("T")){
                if (dateString.contains("+") || dateString.endsWith("Z") || dateString.length() > 19){
                    return OffsetDateTime.parse(dateString, APPLE_DATE_FORMAT).toLocalDateTime();
                }
                return LocalDateTime.parse(dateString, DEFAULT_FORMAT);
            } else if (dateString.contains(" ")){
                return LocalDateTime.parse(dateString, ANDROID_DATE_FORMAT);
            }

            throw new BusinessException(ErrorType.INVALID_PARAMETER);
        } catch (DateTimeParseException e) {
            log.error("[deserialize] error dateString = {}, message = {}", dateString, e.getMessage());
            throw new BusinessException(ErrorType.INVALID_PARAMETER);
        }
    }

}
