package com.spring.fastfood.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.fastfood.enums.VoucherType;
import com.spring.fastfood.validation.VoucherSubset;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherRequest {
    private String code;
    @NotBlank(message = "description can not be blank")
    private String description;
    @Min(value = 1, message = "discount must large than 0")
    private double discount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    @VoucherSubset(anyOf = {VoucherType.VALID,VoucherType.EXPIRED})
    private VoucherType status;

}
