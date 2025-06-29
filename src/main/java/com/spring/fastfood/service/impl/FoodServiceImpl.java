package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.ImageResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.integration.MinioChannel;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.model.*;
import com.spring.fastfood.repository.CategoryRepository;
import com.spring.fastfood.repository.FoodCategoryRepository;
import com.spring.fastfood.repository.FoodRepository;
import com.spring.fastfood.repository.ImageRepository;
import com.spring.fastfood.service.FoodService;
import io.minio.errors.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    private final MinioChannel minioChannel;
    private final ImageRepository imageRepository;


    @Override
    public PageResponse<?> getAllFood(Integer pageNo, Integer pageSize, String sortBy, String keyword) {
        int page = Math.max((pageNo != null ? pageNo : 1) - 1, 0);
        log.info("page: {}",page);
        int size = (pageSize != null ? pageSize : 10);
        log.info("size: {}",size);

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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
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
                    List<ImageResponse> images = new ArrayList<>();
                    for (Image image : food.getImages()){
                        ImageResponse response = new ImageResponse();
                        response.setDataUrl(image.getDataImage());
                        images.add(response);
                    }
                    FoodResponse foodResponse = foodMapper.toFoodResponse(food);
                    foodResponse.setAverageRating(calculateRating(food.getReviews()));
                    foodResponse.setCategories(categoryResponses);
                    foodResponse.setImages(images);
                    foodResponse.setImageFood(food.getImageFood());
                    return foodResponse;
                }).collect(Collectors.toList());

        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
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
    public FoodResponse createFood(FoodRequest request) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        Food food = foodMapper.toFood(request);
        food.setImageFood(minioChannel.update(request.getImage()));
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
        List<Image> images = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile file : request.getImages()) {
                String imageUrl = minioChannel.update(file);
                Image image = Image.builder()
                        .dataImage(imageUrl)
                        .imageName("")
                        .food(foodSaved)
                        .build();
                images.add(image);
            }
            imageRepository.saveAll(images);
        }
        foodCategoryRepository.saveAll(foodCategories);
        foodSaved.setFoodCategories(foodCategories);
        foodSaved.setImages(images);

        FoodResponse foodResponse = foodMapper.toFoodResponse(foodSaved);
        List<ImageResponse> responses = new ArrayList<>();
        for (Image image : foodSaved.getImages()){
            ImageResponse response = new ImageResponse();
            response.setDataUrl(image.getDataImage());
            responses.add(response);
        }
        foodResponse.setImages(responses);
        foodResponse.setImageFood(foodSaved.getImageFood());

        return foodResponse;
    }


    @Override
    @Transactional
    public FoodResponse updateFood(long foodId, FoodRequest request) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        Food food = getFoodById(foodId); // đã tồn tại trong DB
        // Cập nhật các thuộc tính food
        foodMapper.updateFood(food, request); // cập nhật name, price, brand, ...
        // Xoá hết liên kết cũ (do đã khai báo orphanRemoval = true)
        food.getFoodCategories().clear();
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
        // clear ảnh cũ
        food.getImages().clear();
        if (request.getImages() != null && !request.getImages().isEmpty()){
            for (MultipartFile file : request.getImages()){
                String imageUrl = minioChannel.update(file);
                Image image = Image.builder()
                        .food(food)
                        .dataImage(imageUrl)
                        .imageName("")
                        .build();
                food.getImages().add(image);
            }
        }
        Food updatedFood = foodRepository.save(food);
        FoodResponse response = foodMapper.toFoodResponse(updatedFood);
        List<ImageResponse> imageResponses = updatedFood.getImages().stream()
                .map(img -> ImageResponse.builder()
                        .dataUrl(img.getDataImage())
                        .build())
                .collect(Collectors.toList());
        response.setImages(imageResponses);
        return response;
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
        log.info("image: {}" , food.getImageFood());
        FoodResponse response = foodMapper.toFoodResponse(food);
        response.setAverageRating(calculateRating(food.getReviews()));
        response.setNumberAverage(food.getReviews().size());
        return response;
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


















































