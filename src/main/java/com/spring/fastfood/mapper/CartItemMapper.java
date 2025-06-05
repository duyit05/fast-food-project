package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.response.CartItemResponse;
import com.spring.fastfood.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemResponse toCartItemResponse (CartItem cartItem);
    List<CartItemResponse> toListCartItemResponse (List<CartItem> cartItems);
}
