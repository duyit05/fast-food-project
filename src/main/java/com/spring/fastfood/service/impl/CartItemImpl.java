package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.model.Cart;
import com.spring.fastfood.model.CartItem;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.repository.CartItemRepository;
import com.spring.fastfood.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    public void addOrUpdateCartItem (Cart cart, Food food, CartItemRequest request){
        // check cart item đã có food chưa nếu có rồi thì update quantiy and price
        CartItem existimgCartItem = cartItemRepository.findByCartAndFood(cart,food).orElse(null);
        if(existimgCartItem != null){
            int newQuantity = existimgCartItem.getQuantity() + request.getQuantity();
            existimgCartItem.setQuantity(newQuantity);
            existimgCartItem.setPriceAtAddTime(food.getPrice());
            existimgCartItem.setTotalPrice(newQuantity * food.getPrice());
            cartItemRepository.save(existimgCartItem);
        }else{
            CartItem newCartItem = CartItem.builder()
                    .food(food)
                    .cart(cart)
                    .quantity(request.getQuantity())
                    .priceAtAddTime(food.getPrice())
                    .note(request.getNote())
                    .totalPrice(request.getQuantity() * food.getPrice())
                    .build();
            cartItemRepository.save(newCartItem);
        }
    }
}

