package com.github.sunguk0810.assignment.domain.health.dto.device;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Product {
    private String name;
    private String vender;
}
