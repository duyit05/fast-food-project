package com.spring.fastfood.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank(message = "address receive must not be blank")
    private String addressReceive;
    private long shipId;
    private long paymentId;
    private List<OrderDetailRequest> food;

}
