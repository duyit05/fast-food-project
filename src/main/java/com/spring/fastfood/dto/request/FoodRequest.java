package com.spring.fastfood.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodRequest {
    @Size(min = 1, message = "stock must be greater than 0")
    private Integer stock;
    @NotBlank(message = "brand must not be blank")
    private String brand;
    private Double averageRating;
    @NotBlank(message = "food name must not be blank")
    private String foodName;
    @NotBlank(message = "description must not be blank")
    private String description;
    private Double lastedPrice;
    @NotNull(message = "price must not be null")
    @Size(min = 10, message = "price must be greater than 10")
    private Double price;
}
