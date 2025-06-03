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

    @Override
    public CartResponse addToCart(CartItemRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);
        Food food = foodService.getFoodById(request.getFoodId());
        Cart cartOfUser = cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
        CartItem cartAndFood = cartItemRepository.findByCartAndFood(cartOfUser, food).orElse(null);
        if (cartAndFood != null) {
            int newQuantity = cartAndFood.getQuantity() + request.getQuantity();
            cartAndFood.setQuantity(newQuantity);
            cartAndFood.setPriceAtAddTime(food.getPrice());
            cartAndFood.setTotalPrice(newQuantity * food.getPrice());
        } else {
            cartAndFood = CartItem.builder()
                    .cart(cartOfUser)
                    .food(food)
                    .quantity(request.getQuantity())
                    .priceAtAddTime(food.getPrice())
                    .note(request.getNote())
                    .totalPrice(request.getQuantity() * food.getPrice())
                    .build();
            cartOfUser.getCartItems().add(cartAndFood);
        }
        cartRepository.save(cartOfUser);
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItem item : cartOfUser.getCartItems()) {
            Food foodItem = item.getFood();
            FoodResponse foodResponse = FoodResponse.builder()
                    .id(foodItem.getId())
                    .foodName(foodItem.getFoodName())
                    .price(foodItem.getPrice())
                    .brand(foodItem.getBrand())
                    .build();

            CartItemResponse cartItemResponse = CartItemResponse.builder()
                    .food(foodResponse)
                    .quantity(item.getQuantity())
                    .priceAtAddTime(item.getPriceAtAddTime())
                    .note(item.getNote())
                    .totalPrice(item.getTotalPrice())
                    .build();

            cartItemResponses.add(cartItemResponse);
        }

        // Trả về response
        CartResponse response = new CartResponse();
        response.setUsername(username);
        response.setCartItem(cartItemResponses);
        return response;
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
