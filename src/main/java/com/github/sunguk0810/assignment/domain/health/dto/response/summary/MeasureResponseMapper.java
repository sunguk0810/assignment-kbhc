package com.github.sunguk0810.assignment.domain.health.dto.response.summary;

import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.constant.SummaryType;
import com.github.sunguk0810.assignment.domain.health.entity.HealthMeasureSummary;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class MeasureResponseMapper {

    private static final Map<MeasureType, Function<HealthMeasureSummary, HealthSummaryResponse>> mapper = new EnumMap<>(MeasureType.class);

    static {
        mapper.put(MeasureType.STEPS, StepSummaryResponse::of);
        mapper.put(MeasureType.BLOOD_PRESSURE, BloodPressureSummaryResponse::of);
        mapper.put(MeasureType.BLOOD_SUGAR, BloodSugarSummaryResponse::of);
        mapper.put(MeasureType.HEART_RATE, HeartRateSummaryResponse::of);
        mapper.put(MeasureType.OXYGEN_SATURATION, OxygenSaturationSummaryResponse::of);
    }

    public static HealthSummaryResponse mapToDto(HealthMeasureSummary summary) {
        MeasureType type = summary.getMeasureType();
        Function<HealthMeasureSummary, HealthSummaryResponse> function = mapper.get(type);

        if (function == null) {
            throw new IllegalArgumentException("지원하지 않는 측정 타입입니다: " + type);
        }

        return function.apply(summary);
    }

    public static HealthSummaryResponse mapToDto(HealthMeasureSummary summary, SummaryType summaryType){
        HealthSummaryResponse response = mapToDto(summary);

        if (SummaryType.MONTHLY.equals(summaryType)){
            String originalDate = response.getSummaryDate();
            if (originalDate != null && originalDate.length() >= 7){
                response.setSummaryDate(originalDate.substring(0, 7));
            }
        }

        return response;
    }

}
