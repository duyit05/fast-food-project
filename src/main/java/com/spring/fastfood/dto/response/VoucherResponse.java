package com.spring.fastfood.dto.response;

import com.spring.fastfood.enums.VoucherType;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    private String code;
    private String description;
    private double discount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "dd/MM/yyyy" )
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE,pattern = "dd/MM/yyyy" )
    private LocalDate endDate;
    private VoucherType status;
}
