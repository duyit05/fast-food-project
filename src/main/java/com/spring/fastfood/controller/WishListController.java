package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.DataResponseError;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/view-my-wish-list")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<List<WishListResponse>> viewMyWishList (){
        return new DataResponse<>(HttpStatus.OK.value(), "view my wish list",wishListService.userViewMyWishList());
    }

    @DeleteMapping("/delete-wish-list/{wishListId}")
    @PreAuthorize("hasAuthority('USER')")
    public DataResponse<?> deleteWishList(@PathVariable Long wishListId) {
        try {
            wishListService.deleteWishListByUser(wishListId);
            return new DataResponse<>(HttpStatus.BAD_REQUEST.value(), "user delete wish list successfully");
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage = {}" , e.getMessage() , e.getCause());
            return new DataResponseError(HttpStatus.BAD_REQUEST.value(), "user delete wish list fail");
        }
    }
}
