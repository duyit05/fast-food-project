package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wish-list")
@RestController
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/add-to-wish-list")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<WishListResponse> addToWishList(@RequestBody WishListRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(),"add to wishlist",wishListService.addToWishList(request));
    }
}
