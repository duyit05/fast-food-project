package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.repository.FoodRepository;
import com.spring.fastfood.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    @Override
    public List<FoodResponse> getAllFood() {
        return null;
    }

    @Override
    public FoodResponse createFood(FoodRequest request) {
        Food food = foodMapper.toFood(request);
        return foodMapper.toFoodResponse(foodRepository.save(food));
    }

    @Override
    public FoodResponse updateFood(Integer foodId, FoodRequest request) {
        return null;
    }

    @Override
    public void deleteFood(Integer foodId) {

    }
}
