package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "health_measure_summaries", comment = "건강 측정 요약 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureSummary extends BaseEntity {
    @Id
    @Column(comment = "요약 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long summaryId;

    @ManyToOne
    @JoinColumn(name = "record_key", foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_SUMMARY_RECORD_KEY"), comment = "사용자 구분키")
    private User userInfo;

    @Column(comment = "요약일자")
    private LocalDate summaryDate;

    @ManyToOne
    @JoinColumn(name = "item_code", foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_SUMMARY_ITEM_CODE"), referencedColumnName = "item_code", comment = "항목코드")
    private HealthTestItem testItem;

    @Column(comment = "합계")
    private Double totalValue;

    @Column(comment = "평균")
    private Double avgValue;

    @Column(comment = "최소")
    private Double minValue;

    @Column(comment = "최대")
    private Double maxValue;

    @Builder
    public HealthMeasureSummary(User userInfo, LocalDate summaryDate, HealthTestItem testItem, Double totalValue, Double avgValue, Double minValue, Double maxValue) {
        this.userInfo = userInfo;
        this.summaryDate = summaryDate;
        this.testItem = testItem;
        this.totalValue = totalValue;
        this.avgValue = avgValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthMeasureSummary that = (HealthMeasureSummary) o;
        return Objects.equals(summaryId, that.summaryId) && Objects.equals(userInfo, that.userInfo) && Objects.equals(summaryDate, that.summaryDate) && Objects.equals(testItem, that.testItem) && Objects.equals(totalValue, that.totalValue) && Objects.equals(avgValue, that.avgValue) && Objects.equals(minValue, that.minValue) && Objects.equals(maxValue, that.maxValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summaryId, userInfo, summaryDate, testItem, totalValue, avgValue, minValue, maxValue);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureSummary.class.getSimpleName() + "[", "]")
                .add("summaryId=" + summaryId)
                .add("userInfo=" + userInfo)
                .add("summaryDate=" + summaryDate)
                .add("testItem=" + testItem)
                .add("totalValue=" + totalValue)
                .add("avgValue=" + avgValue)
                .add("minValue=" + minValue)
                .add("maxValue=" + maxValue)
                .toString();
    }
}
