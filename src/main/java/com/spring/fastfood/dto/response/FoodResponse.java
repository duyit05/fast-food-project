package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class FoodResponse {
    private Long id;
    private Integer stock;
    private String brand;
    private Double averageRating;
    private String foodName;
    private String description;
    private Double lastedPrice;
    private Double price;
}
