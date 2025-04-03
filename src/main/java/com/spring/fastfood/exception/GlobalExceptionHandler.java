package com.spring.fastfood.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Handle exception when the data invalid. (@RequestBody, @RequestParam, @PathVariable)",
                                    summary = "Handle Bad Request",
                                    value = """
                                            {
                                                 "timestamp": "2024-04-07T11:38:56.368+00:00",
                                                 "status": 400,
                                                 "path": "/api/v1/...",
                                                 "error": "Invalid Payload",
                                                 "message": "{data} must be not blank"
                                             }
                                            """
                            ))})
    })
    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            message = message.substring(start, end);
            errorResponse.setError("Invalid Payload");
            errorResponse.setMessage(message);
        } else if (e instanceof MissingServletRequestParameterException) {
            errorResponse.setError("Invalid Parameter");
            errorResponse.setMessage(message);
        } else if (e instanceof ConstraintViolationException) {
            errorResponse.setError("Invalid Parameter");
            errorResponse.setMessage(message.substring(message.indexOf(" ") + 1));
        } else {
            errorResponse.setError("Invalid Data");
            errorResponse.setMessage(message);
        }

        return errorResponse;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
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
    public ErrorResponse handleResourceNotFoundException(DataIntegrityViolationException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(BAD_REQUEST.value());
        errorResponse.setError(BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
}
