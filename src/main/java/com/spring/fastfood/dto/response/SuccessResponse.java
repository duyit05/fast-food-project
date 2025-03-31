package com.spring.fastfood.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class SuccessResponse extends ResponseEntity<SuccessResponse.Payload> {

    // PUT, PATCH, DELETE
    public SuccessResponse(HttpStatusCode status , String message){
        super(new Payload(status.value() , message), HttpStatus.OK);
    }
    // GET, POST
    public SuccessResponse(HttpStatusCode status , String message , Object data){
        super(new Payload(status.value() , message, data), HttpStatus.OK);
    }
    public static class Payload{
        private final int status;
        private final String message;
        private  Object data;

        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
