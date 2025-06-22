package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.FoodService;
import io.minio.errors.*;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Validated
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<FoodResponse> getAllFood(@ModelAttribute FoodRequest request) throws ServerException,
            InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return new DataResponse<>(HttpStatus.OK.value(), "create food", foodService.createFood(request));
    }

    @GetMapping("/list-food")
    public DataResponse<PageResponse<?>> getAllFood(
            @RequestParam (required = false) Integer pageNo,
            @RequestParam (required = false) Integer pageSize,
            @RequestParam (required = false)String sortBy,
            @RequestParam (required = false) String keyword
            ) {
        return new DataResponse<>(HttpStatus.OK.value(), "foods", foodService.getAllFood(pageNo,pageSize,sortBy,keyword));
    }

    @PutMapping("/{foodId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<FoodResponse> updateFood (@PathVariable long foodId, @Valid @ModelAttribute FoodRequest request)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
            return new DataResponse<>(HttpStatus.ACCEPTED.value(), "update food",foodService.updateFood(foodId,request));
    }

    @DeleteMapping("/{foodId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<?> deleteFood (@PathVariable long foodId){
        try {
            foodService.deleteFood(foodId);
            return new DataResponse<>(HttpStatus.ACCEPTED.value(), "delete food successfully");
        }catch (ResourceNotFoundException e){
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "Food not found id: " + foodId);
        }
    }

    @GetMapping("/{foodId}")
    public DataResponse<FoodResponse> getFoodById (@PathVariable long foodId){
        return new DataResponse<>(HttpStatus.OK.value(), "get detail food",foodService.getDetailFood(foodId));
    }

    @GetMapping("/by-category")
    public DataResponse<List<FoodResponse>> getFoodByCategoryId (@RequestParam long categoryId){
        return new DataResponse<>(HttpStatus.OK.value(), "food by category",foodService.getFoodByCategoryId(categoryId));
    }
}
