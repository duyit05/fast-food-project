package com.spring.fastfood.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckVoucherRequest {
    private String code;
}
