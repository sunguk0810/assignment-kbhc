package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "health_measure_summaries", comment = "건강 측정 요약 테이블")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureSummary extends BaseEntity {
    @Id
    @Column(comment = "요약 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long summaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_key", nullable = false, comment = "사용자 구분 키", foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_SUMMARY_RECORD_KEY"))
    private User user;

    @Column(comment = "요약일자")
    private LocalDate summaryDate;

    @Enumerated(EnumType.STRING)
    @Column(comment = "측정 유형", nullable = false)
    private MeasureType measureType;

    @Column(comment = "합계")
    private Double sumValue;

    @Column(comment = "평균")
    private Double avgValue;

    @Column(comment = "최소값")
    private Double minValue;

    @Column(comment = "최대값")
    private Double maxValue;

    @Column(comment = "측정 건수")
    private int count;


    @Builder
    public HealthMeasureSummary(User user, LocalDate summaryDate, MeasureType measureType, Double sumValue, Double avgValue, Double minValue, Double maxValue, int count) {
        this.user = user;
        this.summaryDate = summaryDate;
        this.measureType = measureType;
        this.sumValue = sumValue != null ? sumValue : 0.0;
        this.avgValue = avgValue != null ? avgValue : 0.0;
        this.minValue = minValue != null ? minValue : 0.0;
        this.maxValue = maxValue != null ? maxValue : 0.0;
        this.count = count;
    }

    public void updateSummary(Double newValue, boolean isAccumulate){
        this.count++;

        if (isAccumulate){
            // 누적형 데이터 (예: 걸음 수) - 기존 값에 더함
            this.sumValue += newValue;
            this.avgValue = ((this.avgValue * (this.count - 1)) + newValue) / this.count;
        } else {
            this.avgValue = ((this.avgValue * (this.count - 1)) + newValue) / this.count;
        }

        // 최소/최대값 경신
        if (this.minValue == 0 || newValue < this.minValue){
            this.minValue = newValue;
        }
        if (newValue > this.maxValue){
            this.maxValue = newValue;
        }
    }

}
