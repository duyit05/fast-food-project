package com.spring.fastfood.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String addressReceive;
    private LocalDate dateOrder;
    private Double shipPrice;
    private Double totalFood;
    private Double totalPrice;
    private String shipName;
    private String paymentMethod;
    private List<OrderDetailResponse> orderDetails;
}
