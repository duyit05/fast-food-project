package com.spring.fastfood.exception;

public class DataExistException extends RuntimeException{
    public DataExistException(String message){
        super(message);
    }
}
