package com.spring.fastfood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DataResponse<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public DataResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DataResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
