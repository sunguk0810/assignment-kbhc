package com.github.sunguk0810.assignment.global.config;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.global.dto.auth.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
/**
 * JPA Auditing(감사) 기능을 활성화하고 설정하는 클래스입니다.
 * <p>
 * 엔티티의 생성일/수정일 자동 관리뿐만 아니라,
 * {@link org.springframework.data.annotation.CreatedBy}와 {@link org.springframework.data.annotation.LastModifiedBy}가
 * 붙은 필드에 현재 로그인한 사용자 아이디를 주입하는 역할을 합니다.
 * </p>
 *
 * @see EnableJpaAuditing
 */
@Slf4j
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
    /**
     * 현재 인증된 사용자(Auditor) 정보를 감지하여 반환하는 빈을 등록합니다.
     * <p>
     * Spring Security의 {@link SecurityContextHolder}에 접근하여 인증 객체를 확인합니다.
     * 로그인하지 않았거나 익명 사용자일 경우 기본값({@code "ANONYMOUS"})을 반환합니다.
     * </p>
     *
     * @return 사용자 아이디(String)를 감싸는 {@link AuditorAware} 구현체
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null ||
                    !authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("ANONYMOUS");
            }


            if (authentication.getPrincipal() instanceof CustomUserDetails userDetails){
                if (userDetails.getUser() != null){
                 return Optional.of(userDetails.getUser().getRecordKey());
                }
            }
            if (authentication.getPrincipal() instanceof User user) {
                log.info("User = {}", user);
                return Optional.of(user.getRecordKey());
            }

            return Optional.ofNullable(authentication.getName());
        };
    }
}
