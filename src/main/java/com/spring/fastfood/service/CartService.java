package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.dto.response.CartResponse;

public interface CartService {
  CartResponse addToCart (CartItemRequest request);
  CartResponse getMyCart ();
  void deleteFromCart (long foodId);
}
