package com.spring.fastfood.mapper;

import com.spring.fastfood.dto.response.CartResponse;
import com.spring.fastfood.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponse toCartResponse (Cart cart);
}
