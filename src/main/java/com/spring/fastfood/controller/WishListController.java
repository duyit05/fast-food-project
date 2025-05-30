package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.WishListService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wish-list")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/add-to-wish-list")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<WishListResponse> addToWishList(@RequestBody WishListRequest request) {
        return new DataResponse<>(HttpStatus.CREATED.value(), "add to wishlist", wishListService.addToWishList(request));
    }
}
