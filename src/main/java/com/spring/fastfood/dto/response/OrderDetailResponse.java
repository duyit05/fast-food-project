package com.spring.fastfood.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private long orderDetailId;
    private long foodId;
    private String foodName;
    private Integer amount;
    private Double unitPrice;
    private Double price;
}
