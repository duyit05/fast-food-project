package com.spring.fastfood.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private FoodResponse food;
    private Integer quantity;
    private Double priceAtAddTime;
    private Double totalPrice;
    private String note;

}
