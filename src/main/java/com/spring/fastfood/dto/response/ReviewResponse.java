package com.spring.fastfood.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private long reviewId;
    private String comment;
    private double rank;
    private FoodResponse food;
    private UserResponse user;
}
