package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private long paymentId;
    private String paymentName;
    private String description;

}
