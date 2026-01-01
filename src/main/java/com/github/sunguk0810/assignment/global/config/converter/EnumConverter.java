package com.github.sunguk0810.assignment.global.config.converter;

import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * 문자열을 Enum 상수로 변환하는 범용 컨버터 팩토리 클래스입니다.
 * <p>
 * HTTP 요청 파라미터나 경로 변수로 전달된 문자열을 대응하는 Enum 상수로 변환하며,
 * 대소문자 구분 없이 특수문자('_', '-')를 무시하는 유연한 매칭 방식을 제공합니다.
 * </p>
 */
@Component
public class EnumConverter implements ConverterFactory<String, Enum> {

    /**
     * 대상 Enum 타입에 맞는 컨버터를 생성하여 반환합니다.
     *
     * @param targetType 변환할 대상 Enum 클래스 타입
     * @param <T>        Enum을 상속받는 제네릭 타입
     * @return {@link GenericEnumConverter} 인스턴스
     */
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new GenericEnumConverter<>(targetType);
    }

    /**
     * 실제 변환 로직을 수행하는 내부 제네릭 컨버터 클래스입니다.
     *
     * @param <T> Enum 타입
     */
    private static class GenericEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public GenericEnumConverter(Class<T> enumType){
            this.enumType = enumType;
        }

        /**
         * 문자열을 정규화하여 Enum 상수로 매칭합니다.
         * <p>
         * 입력 문자열에서 공백을 제거하고, '_', '-' 문자를 삭제한 후
         * 대문자로 변환하여 Enum 상수의 이름과 비교합니다.
         * </p>
         *
         * @param source 변환할 원본 문자열
         * @return 매칭된 Enum 상수, 입력이 비어있으면 {@code null}
         * @throws BusinessException {@link ErrorType#INVALID_PARAMETER} 매칭되는 Enum 상수가 없을 경우
         */
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
