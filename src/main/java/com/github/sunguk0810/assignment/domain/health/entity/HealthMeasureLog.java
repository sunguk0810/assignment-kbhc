package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.DataSourceType;
import com.github.sunguk0810.assignment.global.constant.StatusType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;


/**
 * 외부 기기나 서비스로부터 수집된 건강 데이터를 원본(Raw Data) 형태로 저장하는 엔티티입니다.
 * <p>
 * 다양한 소스(삼성 헬스, 애플 건강 등)의 데이터 형식이 다를 수 있으므로,
 * 유연한 저장을 위해 핵심 데이터는 <b>JSON 포맷</b>으로 저장합니다.
 * 이후 배치를 통해 정형화된 데이터로 가공(Parsing)되는 과정을 거칩니다.
 * </p>
 *
 * @see DataSourceType
 * @see StatusType
 */
@Builder
@Getter
@Entity
@Table(name = "health_measure_logs", comment = "건강 측정 로그 테이블")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HealthMeasureLog extends BaseEntity {

    /**
     * 로그 식별자 (PK)
     */
    @Id
    @Column(comment = "측정 로그 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measureLogId;

    /**
     * 데이터 수집 원천 (Source)
     * <p>예: SAMSUNG_HEALTH, APPLE_HEALTH, DIRECT_INPUT 등</p>
     */
    @Enumerated(EnumType.STRING)
    @Column(comment = "데이터 소스 유형 (삼성 헬스, 애플 건강, ...)", nullable = false)
    private DataSourceType dataSourceType;

    /**
     * 측정을 수행한 사용자와의 연관 관계 ({@link User})
     */
    @ManyToOne
    @JoinColumn(name = "record_key", comment = "사용자 구분 키", nullable = false, foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_LOG_RECORD_KEY"))
    private User userInfo;
    /**
     * 수집된 원본 데이터 (JSON String)
     */
    @Column(comment = "JSON 원본 데이터", columnDefinition = "JSON")
    private String rawJson;

    /**
     * 데이터 처리 상태
     * <p>
     * 기본값: {@link StatusType#PENDING} (대기 중)<br>
     * <b>주의:</b> EnumType.STRING을 사용해야 DB에 문자열로 저장되어 유지보수에 유리합니다.
     * </p>
     */
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(comment = "데이터 처리 상태", nullable = false)
    private StatusType status = StatusType.PENDING;

    /**
     * 데이터 처리 상태를 변경합니다.
     * (예: PENDING -> SUCCESS or ERROR)
     *
     * @param status 변경할 상태값
     */
    public void updateStatus(StatusType status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthMeasureLog that = (HealthMeasureLog) o;
        return Objects.equals(measureLogId, that.measureLogId) && dataSourceType == that.dataSourceType && Objects.equals(userInfo, that.userInfo) && Objects.equals(rawJson, that.rawJson) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(measureLogId, dataSourceType, userInfo, rawJson, status);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureLog.class.getSimpleName() + "[", "]")
                .add("measureLogId=" + measureLogId)
                .add("dataSourceType=" + dataSourceType)
                .add("userInfo=" + userInfo)
//                .add("rawJson='" + rawJson + "'")
                .add("status=" + status)
                .toString();
    }
}
