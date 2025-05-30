package com.spring.fastfood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListResponse {
    private long wishListId;
    private UserResponse user;
    private FoodResponse food;
}
