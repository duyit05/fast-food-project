package com.spring.fastfood.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private String comment;
    private double rank;
    private long foodId;
}

