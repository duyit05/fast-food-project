package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.CategoryRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    PageResponse<?> getAllCategory (int pageNo, int pageSize);
    CategoryResponse createCategory (CategoryRequest request);
    CategoryResponse updateCategory (long categoryId, CategoryRequest request);
    void deleteCategory (long categoryId);
    CategoryResponse getDetail (long categoryId);

    List<FoodResponse> getListProductByCategoryId (long categoryId);
}
