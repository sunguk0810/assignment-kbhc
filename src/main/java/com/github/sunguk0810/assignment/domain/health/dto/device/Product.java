package com.github.sunguk0810.assignment.domain.health.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Schema(description = "디바이스 정보")
public class Product {
    @Schema(description = "디바이스 이름")
    private String name;

    @Schema(description = "디바이스 제조사")
    private String vender;
}
