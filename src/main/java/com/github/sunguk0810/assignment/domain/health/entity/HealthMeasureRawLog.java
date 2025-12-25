package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.DataSourceType;
import com.github.sunguk0810.assignment.global.constant.StatusType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "health_measure_raw_logs", comment = "건강 측정 원본 로그")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureRawLog extends BaseEntity {
    @Id
    @Column(comment = "건강 측정 원본 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rawId;

    @ManyToOne
    @JoinColumn(name = "record_key", nullable = false, comment = "사용자 구분키")
    private User userInfo;

    @Column(nullable = false, comment = "수신일시")
    private LocalDateTime receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, comment = "데이터 출처")
    private DataSourceType dataSourceType;

    @Column(columnDefinition = "JSON", comment = "원본 JSON")
    private String originalJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status", comment = "처리상태", nullable = false)
    private StatusType status;

    @Builder
    public HealthMeasureRawLog(User userInfo, LocalDateTime receivedAt, DataSourceType dataSourceType, String originalJson) {
        this.userInfo = userInfo;
        this.receivedAt = receivedAt;
        this.dataSourceType = dataSourceType;
        this.originalJson = originalJson;
        this.status = StatusType.PENDING;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthMeasureRawLog that = (HealthMeasureRawLog) o;
        return Objects.equals(rawId, that.rawId) && Objects.equals(userInfo, that.userInfo) && Objects.equals(receivedAt, that.receivedAt) && dataSourceType == that.dataSourceType && Objects.equals(originalJson, that.originalJson) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawId, userInfo, receivedAt, dataSourceType, originalJson, status);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureRawLog.class.getSimpleName() + "[", "]")
                .add("rawId=" + rawId)
                .add("userInfo=" + userInfo)
                .add("receivedAt=" + receivedAt)
                .add("dataSourceType=" + dataSourceType)
                .add("originalJson='" + originalJson + "'")
                .add("status=" + status)
                .toString();
    }
}
