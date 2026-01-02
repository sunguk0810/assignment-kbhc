package com.github.sunguk0810.assignment.domain.health.dto.request;

import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
/**
 * 건강 측정 데이터의 요약 정보(Summary)를 조회하기 위한 요청 DTO 클래스입니다.
 * <p>
 * 클라이언트로부터 조회할 <b>측정 유형({@link MeasureType})</b>과 <b>조회 기간(Start ~ End)</b> 정보를 전달받습니다.
 * 주로 Controller 계층에서 Query Parameter(@RequestParam) 또는 Request Body(@RequestBody)로 매핑되어 사용됩니다.
 * </p>
 *
 * @see MeasureType
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(description = "측정 요약 요청 DTO")
public class MeasureSummaryRequest {
    /**
     * 조회할 건강 측정 데이터의 유형
     * <p>
     * 예: {@code STEPS}(걸음 수), {@code BLOOD_PRESSURE}(혈압) 등
     * </p>
     *
     * @see MeasureType
     */
    @NotNull(message = "측정 타입은 필수입니다. (예: STEPS, BLOOD_PRESSURE, ...)")
    @Schema(description = "측정 타입", examples = {"steps", "bloodPressure"})
    private MeasureType type;

    /**
     * 데이터 집계 방식 (일간/월간)
     * <p>
     * 조회하려는 데이터의 기간 단위를 지정합니다.
     * </p>
     * <ul>
     * <li>{@link SummaryType#DAILY} : 일자별 상세 요약 조회</li>
     * <li>{@link SummaryType#MONTHLY} : 월별 통합 요약 조회</li>
     * </ul>
     *
     * @see SummaryType
     */
    @NotNull(message = "집계 방식은 필수입니다. (예: DAILY, MONTHLY)")
    @Schema(description = "집계 방식", examples = {"daily", "monthly"})
    private SummaryType summaryType;

    /**
     * 조회 시작 일자 (Inclusive)
     * <p>
     * 형식: {@code YYYY-MM-DD} (ISO-8601)<br>
     * 예: {@code 2024-11-01}
     * </p>
     */
    @NotNull(message = "조회 시작일자는 필수입니다.")
    @Schema(description = "조회 시작일자", example = "2025-12-01")
    private LocalDate startDate;

    /**
     * 조회 종료 일자 (Inclusive)
     * <p>
     * 형식: {@code YYYY-MM-DD} (ISO-8601)<br>
     * 예: {@code 2024-11-30}
     * </p>
     */
    @NotNull(message = "조회 종료일자는 필수입니다.")
    @Schema(description = "조회 종료일자", example = "2025-12-31")
    private LocalDate endDate;
}
