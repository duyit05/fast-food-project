package com.spring.fastfood.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @NotBlank(message = "payment name can not be blank")
    private String paymentName;
    @NotBlank(message = "description can not be blank")
    private String description;
}
