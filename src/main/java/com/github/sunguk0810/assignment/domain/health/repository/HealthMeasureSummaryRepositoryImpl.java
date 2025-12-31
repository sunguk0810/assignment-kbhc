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

    public List<HealthMeasureSummary> findMonthlyUserSummary(User user, MeasureType measureType, LocalDate startDate, LocalDate endDate) {

        // 쿼리 조합
        String queryFragment = switch (measureType) {
            case STEPS -> """
                    , SUM(hms.sum_steps)             AS steps
                    , SUM(hms.sum_calories)          AS calories
                    , SUM(hms.sum_distance)          AS distance
                    """;
            case BLOOD_PRESSURE -> """
                    , AVG(hms.avg_systolic)          AS systolic
                    , AVG(hms.avg_diastolic)         AS diastolic
                    """;
            case BLOOD_SUGAR -> """
                    , AVG(hms.avg_blood_sugar)       AS bloodSugar
                    """;
            case HEART_RATE -> """
                    , AVG(hms.avg_heart_rate)        AS heartRate
                    """;
            case OXYGEN_SATURATION -> """
                    , AVG(hms.avg_oxygen_saturation) AS oxygenSaturation
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
