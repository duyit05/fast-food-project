package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.model.Food;

import java.util.List;

public interface FoodService {

    List<FoodResponse> getAllFood();
    FoodResponse createFood (FoodRequest request);
    FoodResponse updateFood (Integer foodId, FoodRequest request);
    void deleteFood (Integer foodId);

}
