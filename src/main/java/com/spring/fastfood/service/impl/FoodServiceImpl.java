package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.Review;
import com.spring.fastfood.repository.FoodRepository;
import com.spring.fastfood.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    @Override
    public PageResponse<?> getAllFood(int pageNo, int pageSize, String sortBy, String keyword) {
        int page = Math.max(pageNo - 1, 0);

        // sort
        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                Sort.Direction direction = matcher.group(3).equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC;
                sorts.add(new Sort.Order(direction, matcher.group(1)));
            }
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        // Chuyển String thành list<String>
        Page<Food> foods = StringUtils.hasLength(keyword) ? foodRepository.searchByKeyWord(keyword,pageable)
                                                          : foodRepository.findAll(pageable);
        List<FoodResponse> responses = foods.getContent().stream().map(
                food -> {
                    FoodResponse foodResponse = foodMapper.toFoodResponse(food);
                    foodResponse.setAverageRating(calculateRating(food.getReviews()));
                    return foodResponse;
                }).collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(foods.getTotalPages())
                .items(responses)
                .build();
    }

    private double calculateRating(List<Review> reviews) {
        double rating = 0;
        for (Review review : reviews) {
            rating += review.getRank();
        }
        double average = rating / reviews.size();
        log.info("Rating: {}", average);
        return average;
    }

    @Override
    public FoodResponse createFood(FoodRequest request) {
        Food food = foodMapper.toFood(request);
        return foodMapper.toFoodResponse(foodRepository.save(food));
    }


    @Override
    public FoodResponse updateFood(long foodId, FoodRequest request) {
        Food food = getFoodById(foodId);
        foodMapper.updateFood(food,request);
        return foodMapper.toFoodResponse(foodRepository.save(food));
    }

    @Override
    public void deleteFood(long foodId) {
        foodRepository.deleteById(foodId);
    }

    public Food getFoodById (Long foodId){
        return foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("can't find food id: " + foodId));
    }
}
