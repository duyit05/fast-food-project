package com.spring.fastfood.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class,
                       HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidatorException(Exception exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        System.out.println("Đã chạy vào exception");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));

        String message = exception.getMessage();
        String errorType = "Validation Error";

        // @Valid hoặc @Validated
        if (exception instanceof MethodArgumentNotValidException) {
            message = extractMessageBetweenBrackets(message);
            errorType = "Payload Invalid";
        // @NotNull, @Size, v.v.
        } else if (exception instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorType = "Parameter Invalid";
        // Lỗi khi không thể đọc hoặc parse request body.
        } else if (exception instanceof HttpMessageNotReadableException) {
            message = extractMessageBetweenBrackets(message);
            errorType = "Enum Invalid";
        }
        response.setError(errorType);
        response.setMessage(message);
        return response;
    }

    private String extractMessageBetweenBrackets(String message) {
        int start = message.lastIndexOf("[");
        int end = message.lastIndexOf("]");
        return (start != -1 && end != -1) ? message.substring(start + 1, end) : message;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalServerException(Exception exception, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        System.out.println("Đã chạy vào exception");
        response.setTimestamp(new Date());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        if (exception instanceof MethodArgumentTypeMismatchException) {
            response.setMessage("Failed to convert String to int");
        }
        return response;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "401 Response",
                                    summary = "Handle exception when unauthorized",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 401,
                                              "path": "/api/v1/...",
                                              "error": "Unauthorized",
                                              "message": "Unauthorized"
                                            }
                                            """
                            ))})
    })
    public ErrorResponse handleResourceNotFoundException(BadCredentialsException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(UNAUTHORIZED.value());
        errorResponse.setError(UNAUTHORIZED.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }

    @ExceptionHandler({ForBiddenException.class, AccessDeniedException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "403 Response",
                                    summary = "Handle exception when access forbidden",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 403,
                                              "path": "/api/v1/...",
                                              "error": "Access Dined",
                                              "message": "Access is denied"
                                            }
                                            """
                            ))})
    })
    public ErrorResponse handleAccessDeniedException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(FORBIDDEN.value());
        errorResponse.setError(FORBIDDEN.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "400 Response",
                                    summary = "Handle exception when bad request",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 400,
                                              "path": "/api/v1/...",
                                              "error": "Bad request",
                                              "message": "Bad request"
                                            }
                                            """
                            ))})
    })
    public ErrorResponse handleResourceNotFoundException(IllegalArgumentException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(BAD_REQUEST.value());
        errorResponse.setError(BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
}
