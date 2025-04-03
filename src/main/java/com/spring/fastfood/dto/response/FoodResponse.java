package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodResponse {
    private Integer id;
    private Integer stock;
    private String brand;
    private Double averageRating;
    private String foodName;
    private String description;
    private Double lastedPrice;
    private Double price;
}
