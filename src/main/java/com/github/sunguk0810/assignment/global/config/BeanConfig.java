package com.github.sunguk0810.assignment.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    /**
     * 애플리케이션에서 사용할 {@link PasswordEncoder} 빈을 생성합니다.
     *
     * 이 메서드는 {@link BCryptPasswordEncoder}를 사용하여 비밀번호를 암호화하고,
     * 암호화된 비밀번호를 검증할 수 있는 기능을 제공합니다.
     *
     * @return {@link BCryptPasswordEncoder} 인스턴스를 반환하며, 이는 비밀번호 암호화 및 검증에 사용됩니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
