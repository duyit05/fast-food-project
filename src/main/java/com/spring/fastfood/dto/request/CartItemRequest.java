package com.spring.fastfood.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    private long foodId;
    private Integer quantity;
    private String note;
}
