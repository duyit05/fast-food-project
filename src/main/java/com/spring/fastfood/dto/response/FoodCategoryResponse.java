package com.spring.fastfood.dto.response;

import com.spring.fastfood.model.Category;
import com.spring.fastfood.model.Food;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class FoodCategoryResponse {
    private Food food;
    private Category category;
}
