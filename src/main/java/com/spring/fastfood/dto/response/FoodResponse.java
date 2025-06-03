package com.spring.fastfood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodResponse {
    private Long id;
    private Integer stock;
    private String brand;
    private Double averageRating;
    private String foodName;
    private String description;
    private Double lastedPrice;
    private Double price;
    private List<CategoryResponse> categories;
    private List<ImageResponse> images;
}
