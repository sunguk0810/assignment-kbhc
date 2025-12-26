package com.github.sunguk0810.assignment.domain.health.entity;

import com.github.sunguk0810.assignment.domain.health.constant.DataSourceType;
import com.github.sunguk0810.assignment.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "health_measure_logs", comment = "건강 측정 로그 테이블")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthMeasureLog extends BaseEntity {

    @Id
    @Column(comment = "측정 로그 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measureLogId;

    @Enumerated(EnumType.STRING)
    @Column(comment = "데이터 소스 유형 (삼성 헬스, 애플 건강, ...)")
    private DataSourceType dataSourceType;


}
