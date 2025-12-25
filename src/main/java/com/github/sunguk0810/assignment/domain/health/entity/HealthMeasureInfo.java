package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
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
@Table(name = "health_measure_infos", comment = "건강 측정 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureInfo extends BaseEntity {
    @Id
    @Column(comment = "측정 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measureId;

    @ManyToOne
    @JoinColumn(name = "record_key", nullable = false, foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_INFO_RECORD_KEY"), comment = "사용자 구분 키")
    private User userInfo;

    @Column(nullable = false, comment = "측정일시")
    private LocalDateTime measureAt;

    @ManyToOne
    @JoinColumn(name = "item_code", nullable = false, foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_INFO_ITEM_CODE"), comment = "", referencedColumnName = "item_code")
    private HealthTestItem testItem;

    @Column(comment = "수치형 값")
    private Double measureValue;

    @Column(comment = "문자형 값")
    private String measureStr;

    @Column(comment = "원본 로그 ID")
    private Long rawId;

    @Column(comment = "기타 속성", columnDefinition = "JSON")
    private String attributes;

    @Builder
    public HealthMeasureInfo(User userInfo, LocalDateTime measureAt, HealthTestItem testItem, Double measureValue, String measureStr, Long rawId, String attributes) {
        this.userInfo = userInfo;
        this.measureAt = measureAt;
        this.testItem = testItem;
        this.measureValue = measureValue;
        this.measureStr = measureStr;
        this.rawId = rawId;
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthMeasureInfo that = (HealthMeasureInfo) o;
        return Objects.equals(measureId, that.measureId) && Objects.equals(userInfo, that.userInfo) && Objects.equals(measureAt, that.measureAt) && Objects.equals(testItem, that.testItem) && Objects.equals(measureValue, that.measureValue) && Objects.equals(measureStr, that.measureStr) && Objects.equals(rawId, that.rawId) && Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measureId, userInfo, measureAt, testItem, measureValue, measureStr, rawId, attributes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureInfo.class.getSimpleName() + "[", "]")
                .add("measureId=" + measureId)
                .add("userInfo=" + userInfo)
                .add("measureAt=" + measureAt)
                .add("testItem=" + testItem)
                .add("measureValue=" + measureValue)
                .add("measureStr='" + measureStr + "'")
                .add("rawId=" + rawId)
                .add("attributes='" + attributes + "'")
                .toString();
    }
}
