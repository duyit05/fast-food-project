package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.CategoryRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.model.Category;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.FoodCategory;
import com.spring.fastfood.repository.CategoryRepository;
import com.spring.fastfood.repository.FoodCategoryRepository;
import com.spring.fastfood.service.CategoryService;
import com.spring.fastfood.service.FoodService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FoodCategoryRepository foodCategoryRepository;
    private final FoodMapper foodMapper;
    private final FoodService foodService;

    @Override
    public PageResponse<?> getAllCategory(int pageNo, int pageSize) {
        int page = Math.max(pageNo - 1, 0);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryResponse> responses = categories.getContent().stream().map(
                category -> {
                    List<FoodResponse> foodResponses = category.getFoodCategories().stream().map(
                            foodCategory -> {
                                Food food = foodCategory.getFood();
                                FoodResponse foodResponse = foodMapper.toFoodResponse(food);
                                foodResponse.setAverageRating(foodService.calculateRating(food.getReviews()));
                                return foodResponse;
                            }).collect(Collectors.toList());

                    return CategoryResponse.builder()
                            .id(category.getId())
                            .categoryName(category.getCategoryName())
                            .foods(foodResponses)
                            .build();
                }).collect(Collectors.toList());
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(categories.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .build();
        Category categorySaved = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(categorySaved.getId())
                .categoryName(categorySaved.getCategoryName())
                .build();
    }

    @Override
    public CategoryResponse updateCategory(long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("can't find category id: " + categoryId));
        category.setCategoryName(request.getCategoryName());
        categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("can't find category id: " + categoryId));
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = getCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponse getDetail(long categoryId) {
        Category category = getCategoryById(categoryId);
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

    @Override
    public List<FoodResponse> getListProductByCategoryId(long categoryId) {
        List<Food> foods = foodCategoryRepository.findFoodByCategoryId(categoryId);
        return foods.stream().map(
                food ->
                    FoodResponse.builder()
                            .id(food.getId())
                            .foodName(food.getFoodName())
                            .price(food.getPrice())
                            .lastedPrice(food.getLastedPrice())
                            .stock(food.getStock())
                            .brand(food.getBrand())
                            .description(food.getDescription())
                            .build())
                .collect(Collectors.toList());
    }
}
