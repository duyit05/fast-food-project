package com.spring.fastfood.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private int id;
    private String categoryName;
    private List<FoodResponse> foods;
}
