package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.CategoryRequest;
import com.spring.fastfood.dto.response.CategoryResponse;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/category")
@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<CategoryResponse> createCategory (@Valid @RequestBody CategoryRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "create category",categoryService.createCategory(request));
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<CategoryResponse> updateCategory (@PathVariable long categoryId,@Valid @RequestBody CategoryRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "update category",categoryService.updateCategory(categoryId,request));
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> deleteCategory (@PathVariable long categoryId){
        categoryService.deleteCategory(categoryId);
        return new DataResponse<>(HttpStatus.OK.value(), "delete category successfully");
    }

    @GetMapping("/list")
    public DataResponse<PageResponse<?>> getAllCategory (@RequestParam int pageNo, @RequestParam int pageSize){
        return new DataResponse<>(HttpStatus.OK.value(), "categories",categoryService.getAllCategory(pageNo, pageSize));
    }
}
