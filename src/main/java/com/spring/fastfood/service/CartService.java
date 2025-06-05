package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.CartItemRequest;
import com.spring.fastfood.dto.response.CartResponse;

import java.util.List;

public interface CartService {
  CartResponse addToCart (CartItemRequest request);
  CartResponse getMyCart ();
  void deleteFromCart (long foodId);

  List<CartResponse> getAllCart ();
  void deleteCart (long cartId);

}
