package com.github.sunguk0810.assignment.global.config;

import com.github.sunguk0810.assignment.global.config.converter.EnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC 전반에 대한 설정을 담당하는 구성(Configuration) 클래스입니다.
 * <p>
 * {@link WebMvcConfigurer}를 구현하여 Spring의 기본 MVC 설정을 커스터마이징합니다.
 * 주로 HTTP 요청 파라미터({@code @RequestParam}, {@code @PathVariable})가
 * 컨트롤러의 메서드 인자로 바인딩될 때 사용되는 <b>데이터 변환(Converting)</b> 규칙을 등록합니다.
 * </p>
 *
 * @see WebMvcConfigurer
 * @see EnumConverter
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final EnumConverter enumConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConverter);
    }
}
