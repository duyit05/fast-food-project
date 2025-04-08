package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.PageResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.FoodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Validated
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<FoodResponse> getAllFood(@RequestBody FoodRequest request) {
        return new DataResponse<>(HttpStatus.OK.value(), "create food", foodService.createFood(request));
    }

    @GetMapping("/list-food")
    public DataResponse<PageResponse<?>> getAllFood(
            @RequestParam @Min(1) int pageNo,
            @RequestParam int pageSize,
            @RequestParam (required = false)String sortBy,
            @RequestParam (required = false) String keyword
            ) {
        return new DataResponse<>(HttpStatus.OK.value(), "foods", foodService.getAllFood(pageNo,pageSize,sortBy,keyword));
    }

    @PutMapping("/{foodId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<FoodResponse> updateFood (@PathVariable long foodId, @Valid @RequestBody FoodRequest request){
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
        return new DataResponse<>(HttpStatus.ACCEPTED.value(), "get detail food",foodService.getDetailFood(foodId));
    }
}
