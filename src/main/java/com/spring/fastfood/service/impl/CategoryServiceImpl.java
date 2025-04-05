package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.CategoryRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Category;
import com.spring.fastfood.repository.CategoryRepository;
import com.spring.fastfood.service.CategoryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public PageResponse<?> getAllCategory(int pageNo, int pageSize) {
        int page = Math.max(pageNo - 1, 0);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryResponse> responses = categories.stream().map(
                category -> CategoryResponse.builder()
                                .id(category.getId())
                                .categoryName(category.getCategoryName())
                                .build()).collect(Collectors.toList());
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

    @Override
    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("can't find category id: " + categoryId));
        categoryRepository.delete(category);
    }
}
