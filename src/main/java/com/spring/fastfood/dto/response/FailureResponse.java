package com.spring.fastfood.dto.response;

import org.springframework.http.HttpStatusCode;

public class FailureResponse extends SuccessResponse {
    public FailureResponse(HttpStatusCode status, String message) {
        super(status, message);
    }
}
