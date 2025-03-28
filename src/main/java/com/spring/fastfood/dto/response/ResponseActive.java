package com.spring.fastfood.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ResponseActive {
    private String message;
    private String email;
}
