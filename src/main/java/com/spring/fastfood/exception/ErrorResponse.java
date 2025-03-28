package com.spring.fastfood.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String path;
    private String error;
    private String message;

    public ErrorResponse(String path, String message) {
        this.path = path;
        this.message = message;
    }


}
