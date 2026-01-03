package com.github.sunguk0810.assignment.domain.health.repository;

import com.github.sunguk0810.assignment.domain.auth.entity.User;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import com.github.sunguk0810.assignment.global.config.exception.BusinessException;
import com.github.sunguk0810.assignment.global.constant.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Repository
@Slf4j
@RequiredArgsConstructor
public class HealthMeasureSummaryRepositoryImpl implements HealthMeasureSummaryRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    // Average 내는 BLOOD_PRESSURE, BLOOD_SUGAR, HEART_RATE, OXYGEN_SATURATION
    // 단순 평균에서 가중 평균으로 수정
    // 이유 : 측정치의 값을 단순평균했을 때 다음과 같은 오류가 발생 할 수 있음
    // 예시)
    // SUMMARY_DATE | MEASURE_TYPE | COUNT | AVG_HEART_RATE
    // ----------------------------------------------------
    // 2026-01-02   | HEART_RATE   | 10    | 120
    // 2026-01-03   | HEART_RATE   | 1     | 180
    // 이라는 값이 있을 때 단순 평균, 가중 평균의 값은 다음과 같다.
    // 단순 평균 : 120 + 180 / 2 = 150
    // 가중 평균 : (120 * 10 + 180 * 1) / (10 + 1) = 125.45
    public List<HealthMeasureSummary> findMonthlyUserSummary(User user, MeasureType measureType, LocalDate startDate, LocalDate endDate) {

        // 쿼리 조합
        // 26-01-03 수정
        // 일일 측정 횟수의 편차가 클 수 있기에, 단순 평균대신 가중 평균을 사용
        // 예시 - 심박수) SUM(hms.avg_blood_sugar * hms.count) / SUM(hms.count)
        String queryFragment = switch (measureType) {
            case STEPS -> """
                    , SUM(hms.sum_steps)                                  AS steps
                    , SUM(hms.sum_calories)                               AS calories
                    , SUM(hms.sum_distance)                               AS distance
                    """;
            case BLOOD_PRESSURE -> """
                    , SUM(hms.avg_systolic * hms.count) / SUM(hms.count)  AS systolic
                    , SUM(hms.avg_diastolic * hms.count) / SUM(hms.count) AS diastolic
                    """;
            case BLOOD_SUGAR -> """
                    , SUM(hms.avg_blood_sugar * hms.count) / SUM(hms.count) AS bloodSugar
                    """;
            case HEART_RATE -> """
                    , SUM(hms.avg_heart_rate * hms.count) / SUM(hms.count)  AS heartRate
                    """;
            case OXYGEN_SATURATION -> """
                    , SUM(hms.avg_oxygen_saturation * hms.count) / SUM(hmg.count) AS oxygenSaturation
                    """;
            default -> throw new BusinessException(ErrorType.INVALID_PARAMETER);
        };

        String sql = String.format("""
                SELECT DATE_FORMAT(hms.summary_date, '%%Y-%%m') AS summaryDate
                     , SUM(hms.count) AS count
                     %s
                  FROM health_measure_summaries hms
                 WHERE hms.record_key = ?
                   AND hms.measure_type = ?
                   AND hms.summary_date BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(hms.summary_date, '%%Y-%%m')
                ORDER BY summaryDate
                """, queryFragment);

        return jdbcTemplate.query(sql, summaryRowMapper(user, measureType)
                , user.getRecordKey()
                , measureType.name()
                , startDate
                , endDate);
    }

    private RowMapper<HealthMeasureSummary> summaryRowMapper(User user, MeasureType measureType) {
        return (rs, rowNum) -> {
            String monthStr = rs.getString("summaryDate");
            LocalDate firstDayOfMonth = LocalDate.parse(monthStr + "-01", DateTimeFormatter.ISO_LOCAL_DATE);

            HealthMeasureSummary.HealthMeasureSummaryBuilder builder = HealthMeasureSummary
                    .builder()
                    .user(user)
                    .measureType(measureType)
                    .summaryDate(firstDayOfMonth)
                    .count(rs.getLong("count"));

            switch (measureType) {
                case STEPS -> builder
                        .sumSteps(rs.getLong("steps"))
                        .sumCalories(rs.getDouble("calories"))
                        .sumDistance(rs.getDouble("distance"));
                case BLOOD_PRESSURE -> builder
                        .avgSystolic(Math.round(rs.getDouble("systolic")))
                        .avgDiastolic(Math.round(rs.getDouble("diastolic")));
                case BLOOD_SUGAR -> builder
                        .avgBloodSugar(Math.round(rs.getDouble("bloodSugar")));
                case HEART_RATE -> builder
                        .avgHeartRate(Math.round(rs.getDouble("heartRate")));
                case OXYGEN_SATURATION -> builder
                        .avgOxygenSaturation(rs.getDouble("oxygenSaturation"));
            }
            return builder.build();
        };
    }
}
