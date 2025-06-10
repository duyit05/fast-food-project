package com.spring.fastfood.exception;

public class VoucherStillValidException extends RuntimeException{
    public VoucherStillValidException(String message){
        super(message);
    }
}
