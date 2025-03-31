package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActiveResponse {
    private String message;
    private String email;
}
