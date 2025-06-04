package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.model.Cart;
import com.spring.fastfood.model.Food;

public interface CartItemService {
     void addOrUpdateCartItem (Cart cart, Food food, CartItemRequest request);
}
