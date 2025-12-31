package com.github.sunguk0810.assignment.global.config.converter;

import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Component
public class EnumConverter implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new GenericEnumConverter<>(targetType);
    }

    private static class GenericEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public GenericEnumConverter(Class<T> enumType){
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            if (!StringUtils.hasText(source)){
                return null;
            }

            String normalizedSource = source.trim()
                    .replace("_", "")
                    .replace("-", "")
                    .toUpperCase();
            return Arrays.stream(enumType.getEnumConstants())
                    .filter(e -> e.name()
                            .replace("_", "")
                            .replace("-", "")
                            .toUpperCase()
                            .equals(normalizedSource))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorType.INVALID_PARAMETER));
        }
    }
}
