package com.spring.fastfood.dto.request;

import com.spring.fastfood.enums.ShipType;
import com.spring.fastfood.validation.ShipSubset;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipRequest implements Serializable {
    @NotBlank(message = "description must not be blank")
    private String description;
    @NotBlank(message = "ship name must not be blank")
    private String shipName;
    @Min(value = 1, message = "ship price must greater than 1")
    private Double shipPrice;
    @ShipSubset(anyOf = {ShipType.EXPRESS,ShipType.ECONOMY,ShipType.STANDARD})
    private ShipType type;
}
