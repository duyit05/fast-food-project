package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.FoodRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.service.FoodService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DataResponse<FoodResponse> getAllFood (@RequestBody FoodRequest request){
        return new DataResponse<>(HttpStatus.OK.value(), "foods",foodService.createFood(request));
    }
}
