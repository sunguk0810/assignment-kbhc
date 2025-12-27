package com.github.sunguk0810.assignment.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Redis 데이터베이스 연결 및 저장소 설정을 담당하는 구성 클래스입니다.
 * <p>
 * Spring Data Redis를 활성화하며, 애플리케이션과 Redis 서버 간의
 * 통신을 담당하는 연결 팩토리({@link RedisConnectionFactory})를 정의합니다.
 * </p>
 *
 * @see EnableRedisRepositories
 */
@Configuration
@EnableRedisRepositories(basePackages = "com.github.sunguk0810.assignment.global.repository")
public class RedisConfig {

    /**
     * Redis 서버 호스트 주소
     * <p>
     * 설정 파일(application.yml)의 {@code spring.data.redis.host} 값을 바인딩합니다.
     * </p>
     */
    @Value("${spring.data.redis.host}")
    private String host;

    /**
     * Redis 서버 포트 번호
     * <p>
     * 설정 파일(application.yml)의 {@code spring.data.redis.port} 값을 바인딩합니다.
     * </p>
     */
    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * Redis 연결을 관리하는 {@link RedisConnectionFactory} 빈을 생성합니다.
     * <p>
     * Spring Boot 2.0 이상부터 기본 클라이언트로 채택된 <b>Lettuce</b>를 사용하여 연결을 설정합니다.
     * </p>
     *
     * @return 호스트와 포트 정보가 설정된 {@link LettuceConnectionFactory}
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }


}
