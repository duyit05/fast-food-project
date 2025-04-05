package com.spring.fastfood.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotNull(message = "category name must have be not null")
    private String categoryName;
}
