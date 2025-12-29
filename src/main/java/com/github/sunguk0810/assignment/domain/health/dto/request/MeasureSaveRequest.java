package com.github.sunguk0810.assignment.domain.health.dto.request;

import com.fasterxml.jackson.annotation.*;
import com.github.sunguk0810.assignment.domain.health.constant.MeasureType;
import com.github.sunguk0810.assignment.domain.health.dto.device.DeviceInfo;
import com.github.sunguk0810.assignment.domain.health.dto.measure.*;
import com.github.sunguk0810.assignment.domain.health.entity.common.HealthDetail;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * 건강 측정 데이터 저장을 위한 요청 DTO 클래스입니다.
 * <p>
 * 사용자 측정 데이터, 메타데이터, 장치 정보 등을 포함하며,
 * Jackson 라이브러리를 통해 JSON으로 직렬화/역직렬화됩니다.
 * </p>
 *
 * @see MeasureType
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MeasureSaveRequest.StepRequest.class, names = {MeasureType.Values.STEPS, "steps"}),
        @JsonSubTypes.Type(value = MeasureSaveRequest.BloodPressureRequest.class, names = {MeasureType.Values.BLOOD_PRESSURE, "bloodPressure"}),
        @JsonSubTypes.Type(value = MeasureSaveRequest.HeartRateRequest.class, names = {MeasureType.Values.HEART_RATE, "heartRate"}),
        @JsonSubTypes.Type(value = MeasureSaveRequest.OxygenSaturationRequest.class, names = {MeasureType.Values.OXYGEN_SATURATION, "oxygenSaturation"}),
        @JsonSubTypes.Type(value = MeasureSaveRequest.BloodSugarRequest.class, names = {MeasureType.Values.BLOOD_SUGAR, "bloodSugar"})
})
@Data
public class MeasureSaveRequest<T extends HealthDetail> {
    /**
     * 해당 측정 데이터의 고유 식별 키
     */
    @JsonProperty("recordKey")
    @JsonAlias({"record_key", "recordKey", "recordkey"})
    @NotNull(message = "측정 데이터 고유 식별 키는 필수입니다.")
    private String recordKey;

    /**
     * 측정 데이터의 종류 (예: 혈압, 걸음 수 등)
     * @see MeasureType
     */
    @NotNull(message = "측정 데이터 유형은 필수입니다.")
    private MeasureType type;

    /**
     * 실제 측정 데이터값, 메모, 장치 정보를 포함하는 상세 데이터 객체
     */
    @NotNull(message = "측정 데이터는 필수입니다.")
    private MeasureSaveRequest.Data<T> data;

    /**
     * 데이터 최종 수정 일시
     * <p>포맷: {@code "yyyy-MM-dd HH:mm:ss Z"} (ISO 8601)</p>
     */
    @NotNull(message = "데이터 최종 수정 일시는 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private ZonedDateTime lastUpdate;


    /**
     * 건강 데이터의 상세 정보를 포함하는 내부 클래스입니다.
     * <p>
     * 측정 데이터 항목 리스트, 데이터를 생성한 디바이스 정보, 추가 메모를 저장합니다.
     * 이 클래스는 상위 {@code MeasureSaveRequest} 클래스 내에서 사용되며,
     * 다양한 건강 데이터 유형을 처리하기 위해 {@link HealthDetail}의 하위 구현체 리스트를 포함합니다.
     * </p>
     *
     * 항목:
     * <ul>
     * - entries: 건강 데이터를 나타내는 {@link HealthDetail}의 하위 객체 리스트.
     * - source: 데이터를 생성하거나 제공한 디바이스 또는 소스 애플리케이션의 정보.
     * - memo: 데이터 관련 추가 정보를 설명하는 메모.
     * </ul>
     *
     * @see HealthDetail
     * @see DeviceInfo
     */
    @lombok.Data
    public static class Data<T extends HealthDetail> {
        private List<T> entries;
        private DeviceInfo source;
        private String memo;
    }


    @lombok.Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class StepRequest extends MeasureSaveRequest<Step> {}

    @lombok.Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class BloodPressureRequest extends MeasureSaveRequest<BloodPressure> {}

    @lombok.Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class HeartRateRequest extends MeasureSaveRequest<HeartRate> {}

    @lombok.Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class OxygenSaturationRequest extends MeasureSaveRequest<OxygenSaturation> {}

    @lombok.Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class BloodSugarRequest extends MeasureSaveRequest<BloodSugar> {}
}
