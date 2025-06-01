package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.model.Category;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.FoodCategory;
import com.spring.fastfood.model.Review;
import com.spring.fastfood.repository.CategoryRepository;
import com.spring.fastfood.repository.FoodCategoryRepository;
import com.spring.fastfood.repository.FoodRepository;
import com.spring.fastfood.service.FoodService;
import jakarta.transaction.Transactional;
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
    private final CategoryRepository categoryRepository;
    private final FoodCategoryRepository foodCategoryRepository;


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
        Page<Food> foods = StringUtils.hasLength(keyword) ? foodRepository.searchByKeyWord(keyword, pageable)
                : foodRepository.findAll(pageable);
        List<FoodResponse> responses = foods.getContent().stream().map(
                food -> {
                    List<CategoryResponse> categoryResponses = food.getFoodCategories().stream().map(
                            foodCategory -> {
                                Category category = foodCategory.getCategory();
                                CategoryResponse categoryResponse = new CategoryResponse();
                                categoryResponse.setId(category.getId());
                                categoryResponse.setCategoryName(category.getCategoryName());
                                return categoryResponse;
                            }
                    ).toList();
                    FoodResponse foodResponse = foodMapper.toFoodResponse(food);
                    foodResponse.setAverageRating(calculateRating(food.getReviews()));
                    foodResponse.setCategories(categoryResponses);
                    return foodResponse;
                }).collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(foods.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public double calculateRating(List<Review> reviews) {
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
        Food foodSaved = foodRepository.save(food);

        List<FoodCategory> foodCategories = new ArrayList<>();
        for (Long categoryId : request.getCategoryId()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

            FoodCategory foodCategory = FoodCategory.builder()
                    .food(food)
                    .category(category)
                    .build();
            foodCategories.add(foodCategory);
        }
        foodCategoryRepository.saveAll(foodCategories);
        foodSaved.setFoodCategories(foodCategories);
        return foodMapper.toFoodResponse(foodSaved);
    }


    @Override
    @Transactional
    public FoodResponse updateFood(long foodId, FoodRequest request) {
        Food food = getFoodById(foodId); // đã tồn tại trong DB

        // Cập nhật các thuộc tính food
        foodMapper.updateFood(food, request); // cập nhật name, price, brand, ...

        // Xoá hết liên kết cũ (do đã khai báo orphanRemoval = true)
        food.getFoodCategories().clear();

        // Tạo danh sách mới
        List<FoodCategory> updatedFoodCategories = new ArrayList<>();
        for (Long categoryId : request.getCategoryId()) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

            FoodCategory foodCategory = FoodCategory.builder()
                    .food(food)
                    .category(category)
                    .build();
            updatedFoodCategories.add(foodCategory);
        }

        // Gán list mới vào food (list cũ đã clear ở trên)
        food.getFoodCategories().addAll(updatedFoodCategories);

        // Lưu lại toàn bộ (food sẽ cascade save FoodCategory nếu cần)
        Food updatedFood = foodRepository.save(food);

        return foodMapper.toFoodResponse(updatedFood);
    }

    @Override
    public void deleteFood(long foodId) {
        foodRepository.deleteById(foodId);
    }

    @Override
    public Food getFoodById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("can't find food id: " + foodId));
    }

    @Override
    public FoodResponse getDetailFood(long foodId) {
        Food food = getFoodById(foodId);
        return foodMapper.toFoodResponse(food);
    }

    @Override
    public List<FoodResponse> getFoodByCategoryId(long categoryId) {
        List<Food> foods = foodCategoryRepository.findFoodByCategoryId(categoryId);
        return foods.stream().map(
                food -> FoodResponse.builder()
                        .id(food.getId())
                        .foodName(food.getFoodName())
                        .price(food.getPrice())
                        .lastedPrice(food.getLastedPrice())
                        .stock(food.getStock())
                        .brand(food.getBrand())
                        .description(food.getDescription())
                        .build()
        ).collect(Collectors.toList());
    }
}
