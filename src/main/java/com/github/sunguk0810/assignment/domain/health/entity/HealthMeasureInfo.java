package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import com.github.sunguk0810.assignment.domain.health.entity.common.Period;
import com.github.sunguk0810.assignment.global.config.converter.HealthDetailConverter;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.StringJoiner;
/**
 * 사용자의 건강 측정 데이터를 저장하는 엔티티 클래스입니다.
 * <p>
 * 건강 측정 기록에 대한 주요 정보를 포함하며, 측정 유형, 상세 정보 및 기간 데이터를 관리합니다.
 * {@link BaseEntity}를 확장하여 생성/수정 날짜 정보를 상속받습니다.
 * </p>
 *
 * @see User
 * @see MeasureType
 * @see HealthDetail
 */
@Getter
@Entity
@Table(name = "health_measure_infos", comment = "건강 측정 테이블")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureInfo extends BaseEntity {
    /**
     * 각 건강 측정 데이터를 고유하게 식별하기 위한 ID (PK)
     */
    @Id
    @Column(comment = "측정 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measureId;

    /**
     * 관련된 측정 로그와의 관계를 나타내는 ID (Logical FK)
     * <p>참조 테이블: {@code health_measure_logs}</p>
     */
    @Column(comment = "측정 로그 ID (health_measure_logs)")
    private Long measureLogId;

    /**
     * 측정을 수행한 사용자와의 연관 관계 ({@link User})
     */
    @ManyToOne
    @JoinColumn(name = "record_key", comment = "사용자 구분 키", nullable = false, foreignKey = @ForeignKey(name = "FK_HEALTH_MEASURE_INFO_RECORD_KEY"))
    private User userInfo;

    /**
     * 측정 타입 (예: 걸음 수, 혈압, 혈당 등)
     * @see MeasureType
     */
    @Enumerated(EnumType.STRING)
    @Column(comment = "측정 타입 (걸음, 혈압 등)", nullable = false)
    private MeasureType measureType;

    /**
     * 각 측정 기록에 대한 상세 정보 객체
     * <p>DB에는 JSON 형식으로 저장되며, {@link HealthDetailConverter}를 통해 객체로 변환됩니다.</p>
     */
    @Column(columnDefinition = "JSON", comment = "건강 측정 정보 상세 JSON")
    @Convert(converter = HealthDetailConverter.class)
    private HealthDetail measureDetail;

    /**
     * 측정 기간 정보 (시작일, 종료일)
     * <p>{@link Period} 임베디드 타입을 사용하여 매핑됩니다.</p>
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fromDate", column = @Column(name = "measure_from_date", comment = "측정시작일자")),
            @AttributeOverride(name = "toDate", column = @Column(name = "measure_to_date", comment = "측정종료일자"))
    })
    private Period period;

    /**
     * HealthMeasureInfo 생성자 (Builder 패턴)
     *
     * @param userInfo      측정을 수행한 사용자 ({@link User})
     * @param measureType   측정 유형 ({@link MeasureType})
     * @param measureDetail 상세 측정 데이터 ({@link HealthDetail})
     * @param period        측정 기간 ({@link Period})
     */
    @Builder
    public HealthMeasureInfo(User userInfo, MeasureType measureType, HealthDetail measureDetail, Period period) {
        this.userInfo = userInfo;
        this.measureType = measureType;
        this.measureDetail = measureDetail;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HealthMeasureInfo that = (HealthMeasureInfo) o;
        return Objects.equals(measureId, that.measureId) && Objects.equals(userInfo, that.userInfo) && measureType == that.measureType && Objects.equals(measureDetail, that.measureDetail) && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measureId, userInfo, measureType, measureDetail, period);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthMeasureInfo.class.getSimpleName() + "[", "]")
                .add("measureId=" + measureId)
                .add("userInfo=" + userInfo)
                .add("measureType=" + measureType)
                .add("measureDetail=" + measureDetail)
                .add("period=" + period)
                .toString();
    }
}
