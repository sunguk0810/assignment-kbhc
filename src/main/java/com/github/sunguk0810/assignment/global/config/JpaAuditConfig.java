package com.github.sunguk0810.assignment.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * JPA 감사(Audit) 기능을 활성화하고 감사를 수행할 사용자 정보를 제공하는 설정 클래스입니다.
 *
 * 이 클래스는 JPA 엔티티 변경 내역을 자동으로 기록하기 위해 Auditing 기능을 활성화합니다.
 * 이를 통해 생성자, 수정자와 같은 정보를 기록하거나 추적할 수 있습니다.
 *
 * 주요 기능:
 * 1. @EnableJpaAuditing를 통해 JPA Auditing 활성화.
 * 2. Bean 정의를 통해 AuditorAware를 구현하여 현재 인증된 사용자의 정보를 제공.
 *    - 인증 정보가 없을 경우 "ANONYMOUS"로 기본값 설정.
 *    - 인증된 사용자가 있을 경우 해당 사용자의 이름을 반환.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null){
                return Optional.of("ANONYMOUS");
            }
            if (authentication != null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                return Optional.of("ANONYMOUS");
            }
            return Optional.ofNullable(authentication.getName());
        };
    }
}
