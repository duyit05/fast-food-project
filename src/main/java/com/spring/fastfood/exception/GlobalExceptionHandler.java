package com.spring.fastfood.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class , ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidatorException (Exception exception , WebRequest request){
        ErrorResponse response = new ErrorResponse();
        System.out.println("Đã chạy vào exception");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=",""));

        String message = exception.getMessage();
        if(exception instanceof MethodArgumentNotValidException){
            int start = message.lastIndexOf("[");
            int end = message.lastIndexOf("]");
            message = message.substring(start + 1, end - 1);
            response.setError("Payload Invalid");
        } else if (exception instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            response.setError("Parameter Invalid");
        }
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalServerException (Exception exception , WebRequest request){
        ErrorResponse response = new ErrorResponse();
        System.out.println("Đã chạy vào exception");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=",""));
        response.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        if(exception instanceof  MethodArgumentTypeMismatchException){
            response.setMessage("Failed to convert String to int");
        }
        return response;
    }
}
