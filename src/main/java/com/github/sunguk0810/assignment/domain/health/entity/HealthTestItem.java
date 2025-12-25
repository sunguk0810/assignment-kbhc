package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.health.constant.TestDataType;
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
@Table(name = "health_test_items",
        comment = "검사항목관리 테이블",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"item_code"}, name = "UK_HEALTH_TEST_ITEM_CODE")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthTestItem extends BaseEntity {
    @Id
    @Column(comment = "항목 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name="item_code", comment = "항목코드", nullable = false, unique = true, length = 10)
    private String itemCode;

    @Column(comment = "항목명", nullable = false)
    private String itemName;

    @Column(comment = "항목 단위")
    private String itemUnit;

    @Enumerated(EnumType.STRING)
    @Column(comment = "데이터 타입", nullable = false)
    private TestDataType dataType;

    @Column(comment = "시작일자", nullable = false)
    private LocalDate validStartDate;

    @Column(comment = "종료일자", nullable = false)
    private LocalDate validEndDate;
    @Builder
    public HealthTestItem(String itemCode, String itemName, String itemUnit, TestDataType dataType, LocalDate validStartDate, LocalDate validEndDate) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemUnit = itemUnit;
        this.dataType = dataType;
        this.validStartDate = validStartDate;
        this.validEndDate = validEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthTestItem that = (HealthTestItem) o;
        return Objects.equals(itemId, that.itemId) && Objects.equals(itemCode, that.itemCode) && Objects.equals(itemName, that.itemName) && Objects.equals(itemUnit, that.itemUnit) && dataType == that.dataType && Objects.equals(validStartDate, that.validStartDate) && Objects.equals(validEndDate, that.validEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemCode, itemName, itemUnit, dataType, validStartDate, validEndDate);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthTestItem.class.getSimpleName() + "[", "]")
                .add("itemId=" + itemId)
                .add("itemCode='" + itemCode + "'")
                .add("itemName='" + itemName + "'")
                .add("itemUnit='" + itemUnit + "'")
                .add("dataType=" + dataType)
                .add("validStartDate=" + validStartDate)
                .add("validEndDate=" + validEndDate)
                .toString();
    }
}
