package com.spring.fastfood.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    private  long foodId;
    private Integer amount;
}
