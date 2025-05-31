package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.WishListResponse;

import java.util.List;

public interface WishListService {
    WishListResponse addToWishList (WishListRequest request);
     List<WishListResponse> userViewMyWishList();
    void deleteWishListByUser (long wishListId);
}
