package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.dto.measure.*;
import com.github.sunguk0810.assignment.domain.health.entity.common.Quantity;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.StringJoiner;

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

    // ===== Summary 테이블에는 핵심 데이터만 요약 =====
    // ===== 1. 합계 데이터 필드 정의 =====
    @Column(comment = "총 걸음 수 (단위 : 보)", nullable = false)
    private Long sumSteps;

    @Column(comment = "총 칼로리 (단위 : kcal)", nullable = false)
    private Double sumCalories;

    @Column(comment = "총 거리 (단위 : km)", nullable = false)
    private Double sumDistance;

    // ===== 2. 평균 데이터 필드 정의 =====
    @Column(comment = "평균 수축기 혈압 (단위 : mmHg)", nullable = false)
    private Long avgSystolic;

    @Column(comment = "평균 이완기 혈압 (단위 : mmHg)", nullable = false)
    private Long avgDiastolic;

    @Column(comment = "평균 혈당 (단위 : mg/dL)", nullable = false)
    private Long avgBloodSugar;

    @Column(comment = "평균 심박수 (단위 : bpm)", nullable = false)
    private Long avgHeartRate;

    @Column(comment = "평균 산소포화도 (단위 : %)", nullable = false)
    private Double avgOxygenSaturation;

    @Column(comment = "측정 건수")
    private Long count;

    @Builder
    public HealthMeasureSummary(User user,
                                LocalDate summaryDate,
                                MeasureType measureType,
                                Long sumSteps,
                                Double sumCalories,
                                Double sumDistance,
                                Long avgSystolic,
                                Long avgDiastolic,
                                Long avgBloodSugar,
                                Long avgHeartRate,
                                Double avgOxygenSaturation,
                                Long count) {
        this.user = user;
        this.summaryDate = summaryDate;
        this.measureType = measureType;
        this.sumSteps = sumSteps != null ? sumSteps : 0L;
        this.sumCalories = sumCalories != null ? sumCalories : 0.0;
        this.sumDistance = sumDistance != null ? sumDistance : 0.0;
        this.avgSystolic = avgSystolic != null ? avgSystolic : 0L;
        this.avgDiastolic = avgDiastolic != null ? avgDiastolic : 0L;
        this.avgBloodSugar = avgBloodSugar != null ? avgBloodSugar : 0L;
        this.avgHeartRate = avgHeartRate != null ? avgHeartRate : 0L;
        this.avgOxygenSaturation = avgOxygenSaturation != null ? avgOxygenSaturation : 0.0;
        this.count = count != null ? count : 0L;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureSummary.class.getSimpleName() + "[", "]")
                .add("summaryId=" + summaryId)
                .add("measureType=" + measureType)
                .add("summaryDate=" + summaryDate)
                .add("sumSteps=" + sumSteps)
                .add("sumCalories=" + sumCalories)
                .add("sumDistance=" + sumDistance)
                .add("avgSystolic=" + avgSystolic)
                .add("avgDiastolic=" + avgDiastolic)
                .add("avgBloodSugar=" + avgBloodSugar)
                .add("avgHeartRate=" + avgHeartRate)
                .add("avgOxygenSaturation=" + avgOxygenSaturation)
                .add("count=" + count)
                .toString();
    }

    /**
     * 새로운 <b>걸음 수(Step)</b> 데이터를 요약 정보에 반영합니다.
     * <p>
     * 단순 합산 방식을 사용하며, 걸음 수, 칼로리, 거리를 모두 누적합니다.
     * </p>
     *
     * @param step 반영할 걸음 수 데이터 객체 ({@link Step})
     */
    public void updateSummary(Step step){
        this.count++;

        Double steps = step.getSteps() != null ? step.getSteps() : 0.0;
        Double calories = Quantity.getValueFromQuantity(step.getCalories());
        Double distance = Quantity.getValueFromQuantity(step.getDistance());

        this.sumSteps += steps.intValue();
        this.sumCalories += calories;
        this.sumDistance += distance;
    }

    /**
     * 새로운 <b>혈압(BloodPressure)</b> 데이터를 요약 정보에 반영합니다.
     *
     * @param bp 반영할 혈압 데이터 객체 ({@link BloodPressure})
     */
    public void updateSummary(BloodPressure bp){
        this.count++;

        Double systolic = Quantity.getValueFromQuantity(bp.getSystolic());
        Double diastolic = Quantity.getValueFromQuantity(bp.getDiastolic());

        this.avgSystolic = Math.round(this.avgSystolic + (systolic - this.avgSystolic) / this.count);
        this.avgDiastolic = Math.round(this.avgDiastolic + (diastolic - this.avgDiastolic) / this.count);

    }

    /**
     * 새로운 <b>혈당(BloodSugar)</b> 데이터를 요약 정보에 반영합니다.
     *
     * @param bs 반영할 혈당 데이터 객체 ({@link BloodSugar})
     */
    public void updateSummary(BloodSugar bs) {
        this.count++;

        Double bloodSugar = Quantity.getValueFromQuantity(bs.getBloodSugar());

        this.avgBloodSugar = Math.round(this.avgBloodSugar + (bloodSugar - this.avgBloodSugar) / this.count);
    }

    /**
     * 새로운 <b>심박수(HeartRate)</b> 데이터를 요약 정보에 반영합니다.
     *
     * @param hr 반영할 심박수 데이터 객체 ({@link HeartRate})
     */
    public void updateSummary(HeartRate hr){
        this.count++;

        Double heartRate = Quantity.getValueFromQuantity(hr.getHeartRate());

        this.avgHeartRate = Math.round(this.avgHeartRate + (heartRate - this.avgHeartRate) / this.count);
    }
    /**
     * 새로운 <b>산소포화도(OxygenSaturation)</b> 데이터를 요약 정보에 반영합니다.
     *
     * @param os 반영할 산소포화도 데이터 객체 ({@link OxygenSaturation})
     */
    public void updateSummary(OxygenSaturation os){
        this.count++;

        Double oxygenSaturation = Quantity.getValueFromQuantity(os.getOxygenSaturation());
        this.avgOxygenSaturation = this.avgOxygenSaturation + (oxygenSaturation - this.avgOxygenSaturation) / this.count;
    }







}
