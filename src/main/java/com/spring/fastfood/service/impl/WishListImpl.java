package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.WishListRequest;
import com.spring.fastfood.dto.response.WishListResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.FoodMapper;
import com.spring.fastfood.mapper.UserMapper;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.User;
import com.spring.fastfood.model.WishList;
import com.spring.fastfood.repository.WishListRepository;
import com.spring.fastfood.service.FoodService;
import com.spring.fastfood.service.UserService;
import com.spring.fastfood.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final UserService userService;
    private final FoodService foodService;
    private final FoodMapper foodMapper;
    private final UserMapper userMapper;

    @Override
    public WishListResponse addToWishList(WishListRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username: " + username);
        User user = userService.findByUsername(username);
        Food food = foodService.getFoodById(request.getFoodId());

        if (wishListRepository.findByUserAndFood(user, food).isPresent()) {
            throw new ResourceNotFoundException("food already in wishlist");
        }
        WishList wishList = WishList.builder()
                .user(user)
                .food(food)
                .build();
        wishListRepository.save(wishList);
        return WishListResponse.builder()
                .wishListId(wishList.getId())
                .user(userMapper.toUserResponse(wishList.getUser()))
                .food(foodMapper.toFoodResponse(wishList.getFood()))
                .build();
    }

    @Override
    public List<WishListResponse> userViewMyWishList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        List<WishList> wishLists = wishListRepository.findByUser(user);
        return wishLists.stream().map(
                wishList ->
                        WishListResponse.builder()
                                .wishListId(wishList.getId())
                                .food(foodMapper.toFoodResponse(wishList.getFood()))
                                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void deleteWishListByUser(long wishListId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        WishList wishList = wishListRepository.findByIdAndUser(wishListId, user)
                .orElseThrow(() -> new ResourceNotFoundException("wishlist not found or access denied"));
        wishListRepository.delete(wishList);
    }
}
