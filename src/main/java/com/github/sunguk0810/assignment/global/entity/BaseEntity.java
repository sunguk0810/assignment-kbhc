package com.github.sunguk0810.assignment.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티에서 공통으로 사용되는 데이터 추적(Auditing)용 추상 클래스입니다.
 * <p>
 * {@link EntityListeners}를 통해 엔티티의 생성/수정 시점과 주체(사용자)를 자동으로 기록합니다.
 * 실제 테이블로 매핑되지 않고, 상속받는 자식 엔티티에게 매핑 정보만 제공합니다.
 * </p>
 *
 * @see AuditingEntityListener
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    /**
     * 엔티티 생성 일시
     * <p>
     * {@link CreatedDate}에 의해 데이터 생성 시 자동으로 설정되며, 이후 수정되지 않습니다.
     * </p>
     */
    @CreatedDate
    @Column(updatable = false, comment = "생성일자", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    /**
     * 엔티티 최종 수정 일시
     * <p>
     * {@link LastModifiedDate}에 의해 데이터가 변경될 때마다 자동으로 갱신됩니다.
     * </p>
     */
    @LastModifiedDate
    @Column(comment = "수정일자", nullable = false,  columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    /**
     * 엔티티 생성자 (ID 또는 식별자)
     * <p>
     * {@link CreatedBy}에 의해 생성 시점의 인증된 사용자 정보가 저장됩니다.
     * </p>
     */
    @CreatedBy
    @Column(updatable = false, comment = "생성자", columnDefinition = "VARCHAR(255) DEFAULT 'ANONYMOUS'")
    private String createdBy;

    /**
     * 엔티티 최종 수정자 (ID 또는 식별자)
     * <p>
     * {@link LastModifiedBy}에 의해 수정 시점의 인증된 사용자 정보가 저장됩니다.
     * </p>
     */
    @LastModifiedBy
    @Column(comment = "수정자", columnDefinition = "VARCHAR(255) DEFAULT 'ANONYMOUS'")
    private String updatedBy;
}
