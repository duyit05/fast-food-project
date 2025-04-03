package com.spring.fastfood.dto.response;

public class DataResponseError extends DataResponse {
    public DataResponseError(int status , String message){
        super(status , message);
    }


}
