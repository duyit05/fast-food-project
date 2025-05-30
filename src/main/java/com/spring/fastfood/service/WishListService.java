package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.WishListResponse;

public interface WishListService {
    WishListResponse addToWishList (WishListRequest request);

}
