package com.spring.fastfood.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    @NotBlank(message = "comment must not be blank")
    private String comment;
    @Min(value = 1 , message = "rank must greater than 1")
    private double rank;
    private long foodId;
}

