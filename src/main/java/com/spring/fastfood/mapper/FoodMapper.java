package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.model.Food;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    Food toFood (FoodRequest request);
    FoodResponse toFoodResponse (Food food);
    void updateFood (@MappingTarget Food food, FoodRequest request);
}
