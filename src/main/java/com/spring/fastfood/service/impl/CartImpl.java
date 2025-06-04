package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.dto.response.CartItemResponse;
import com.spring.fastfood.dto.response.CartResponse;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.mapper.CartMapper;
import com.spring.fastfood.model.Cart;
import com.spring.fastfood.model.CartItem;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.CartItemRepository;
import com.spring.fastfood.repository.CartRepository;
import com.spring.fastfood.service.CartItemService;
import com.spring.fastfood.service.CartService;
import com.spring.fastfood.service.FoodService;
import com.spring.fastfood.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Repository
@Slf4j
@RequiredArgsConstructor
public class CartImpl implements CartService {
    private final UserService userService;
    private final FoodService foodService;
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;

    @Override
    public CartResponse addToCart(CartItemRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Food food = foodService.getFoodById(request.getFoodId());
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart cartUser = new Cart();
            cartUser.setUser(user);
            return cartRepository.save(cartUser);
        });

        cartItemService.addOrUpdateCartItem(cart,food,request);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        List<CartItemResponse> responses = cartItems.stream().map(
                item -> {
                    Food foodItem = item.getFood();
                    FoodResponse foodResponse = FoodResponse
                            .builder()
                            .id(foodItem.getId())
                            .foodName(foodItem.getFoodName())
                            .price(foodItem.getPrice())
                            .brand(foodItem.getBrand())
                            .build();
                    return CartItemResponse.builder()
                            .food(foodResponse)
                            .quantity(item.getQuantity())
                            .priceAtAddTime(item.getPriceAtAddTime())
                            .note(item.getNote())
                            .totalPrice(item.getTotalPrice())
                            .build();
                }
        ).collect(Collectors.toList());

        return CartResponse.builder()
                .username(username)
                .cartItem(responses)
                .size(responses.size())
                .build();

//        List<CartItemResponse> cartItemResponses = new ArrayList<>();
//        for (CartItem item : cartOfUser.getCartItems()) {
//            Food foodItem = item.getFood();
//            FoodResponse foodResponse = FoodResponse.builder()
//                    .id(foodItem.getId())
//                    .foodName(foodItem.getFoodName())
//                    .price(foodItem.getPrice())
//                    .brand(foodItem.getBrand())
//                    .build();
//
//            CartItemResponse cartItemResponse = CartItemResponse.builder()
//                    .food(foodResponse)
//                    .quantity(item.getQuantity())
//                    .priceAtAddTime(item.getPriceAtAddTime())
//                    .note(item.getNote())
//                    .totalPrice(item.getTotalPrice())
//                    .build();
//
//            cartItemResponses.add(cartItemResponse);
//        }
//
//        // Trả về response
//        CartResponse response = new CartResponse();
//        response.setUsername(username);
//        response.setCartItem(cartItemResponses);
//        response.setSize(cartItemResponses.size());
//        return response;
    }

    public Cart findCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("cart not found"));
    }

    @Override
    public CartResponse getMyCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Cart cart = findCartByUser(user);
        List<CartItemResponse> cartItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            Food food = item.getFood();
            FoodResponse foodResponse = new FoodResponse();
            foodResponse.setId(food.getId());
            foodResponse.setBrand(food.getBrand());
            foodResponse.setDescription(food.getDescription());
            foodResponse.setPrice(food.getPrice());

            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setFood(foodResponse);
            cartItemResponse.setQuantity(item.getQuantity());
            cartItemResponse.setPriceAtAddTime(food.getPrice());
            cartItemResponse.setTotalPrice(item.getTotalPrice());
            cartItemResponse.setNote(item.getNote());

            cartItems.add(cartItemResponse);
        }

        CartResponse response = new CartResponse();
        response.setUsername(username);
        response.setCartItem(cartItems);
        response.setSize(cartItems.size());
        return response;
    }

    @Override
    public void deleteFromCart(long foodId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Food checkFood = foodService.getFoodById(foodId);
        Cart cart = findCartByUser(user);
        CartItem cartItem = cartItemRepository.findByCartAndFood(cart, checkFood)
                .orElseThrow(() -> new ResourceNotFoundException("cart or food not found"));
        cart.getCartItems().remove(cartItem);
        cartRepository.save(cart);
    }
}
