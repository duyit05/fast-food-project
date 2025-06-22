package com.spring.fastfood.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.fastfood.model.Review;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private Long id;
    private Integer stock;
    private String brand;
    private Double averageRating;
    private Integer numberAverage;
    private String foodName;
    private String description;
    private Double lastedPrice;
    private Double price;
    private String imageFood;
    private List<CategoryResponse> categories;
    private List<ImageResponse> images;
}
