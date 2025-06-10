package com.spring.fastfood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {
    private long reviewId;
    private String comment;
    private double rank;
    private FoodResponse food;
    private UserResponse user;
}
