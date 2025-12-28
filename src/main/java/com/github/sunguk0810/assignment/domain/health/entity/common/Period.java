package com.github.sunguk0810.assignment.domain.health.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Column(comment = "측정시작일자", name = "from_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime from;

    /**
     * 측정 종료 일시
     */
    @Column(comment = "측정종료일자", name = "to_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime to;
    /**
     * Period 생성자 (Builder 패턴)
     * <p>
     * 시작 시간이 종료 시간보다 늦을 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param from 측정 시작 일시
     * @param to   측정 종료 일시
     * @throws IllegalArgumentException {@code from}가 {@code toDate}보다 이후일 경우 발생
     */
    @Builder
    public Period(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("시작 시간은 종료시간보다 앞서야 합니다.");
        }
        this.from = from;
        this.to = to;
    }
}
