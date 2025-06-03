package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.Review;
import io.minio.errors.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FoodService {

    PageResponse<?> getAllFood(int pageNo, int pageSize, String sortBy, String keyword);
    FoodResponse createFood (FoodRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    FoodResponse updateFood (long foodId, FoodRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    void deleteFood (long foodId);

    FoodResponse getDetailFood (long foodId);
     double calculateRating(List<Review> reviews);

    Food getFoodById(Long foodId);
    List<FoodResponse> getFoodByCategoryId (long categoryId);

}
