package com.spring.fastfood.dto.response;

import com.spring.fastfood.enums.ShipType;
import com.spring.fastfood.validation.ShipSubset;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipResponse {
    private long shipId;
    private String description;
    private String shipName;
    private Double shipPrice;
    private ShipType type;
}
