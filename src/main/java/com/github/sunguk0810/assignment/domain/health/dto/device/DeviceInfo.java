package com.github.sunguk0810.assignment.domain.health.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * 건강 데이터를 생성한 디바이스 또는 소스 애플리케이션의 정보를 담는 클래스입니다.
 * <p>
 * 측정 디바이스의 모드, 식별 이름, 타입 및 상세 제품 정보({@link Product})를 포함합니다.
 * </p>
 *
 * @see Product
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "디바이스 정보")
public class DeviceInfo {
    /**
     * 디바이스의 모드 식별 값
     * <p>예: {@code 9}</p>
     */
    @Schema(description = "디바이스 모드 식별 값")
    private Long mode;

    /**
     * 디바이스 또는 데이터 소스 애플리케이션 이름
     * <p>예: {@code "SamsungHealth"}</p>
     */
    @Schema(description = "애플리케이션 이름", examples = {"SamsungHealth", "Health Kit"})
    private String name;

    /**
     * 디바이스의 유형 (타입)
     */
    @Schema(description = "타입")
    private String type;

    /**
     * 디바이스와 연관된 하드웨어/제품 상세 정보
     * @see Product
     */
    @Schema(description = "디바이스 정보")
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeviceInfo that = (DeviceInfo) o;
        return Objects.equals(mode, that.mode) && Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, name, type, product);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceInfo.class.getSimpleName() + "[", "]")
                .add("mode=" + mode)
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("product=" + product)
                .toString();
    }
}
