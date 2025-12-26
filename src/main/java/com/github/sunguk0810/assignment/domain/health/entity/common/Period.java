package com.github.sunguk0810.assignment.domain.health.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 측정 기간(시작 시점 ~ 종료 시점)을 나타내는 임베디드 값 객체(Value Object)입니다.
 * <p>
 * 불변(Immutable) 객체로 설계되었으며, 데이터 무결성을 위해 생성 시점에
 * 시작 시간이 종료 시간보다 늦지 않은지 검증합니다.
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    /**
     * 측정 시작 일시
     */
    @Column(comment = "측정시작일자")
    private LocalDateTime fromDate;

    /**
     * 측정 종료 일시
     */
    @Column(comment = "측정종료일자")
    private LocalDateTime toDate;
    /**
     * Period 생성자 (Builder 패턴)
     * <p>
     * 시작 시간이 종료 시간보다 늦을 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param fromDate 측정 시작 일시
     * @param toDate   측정 종료 일시
     * @throws IllegalArgumentException {@code fromDate}가 {@code toDate}보다 이후일 경우 발생
     */
    @Builder
    public Period(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("시작 시간은 종료시간보다 앞서야 합니다.");
        }
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
