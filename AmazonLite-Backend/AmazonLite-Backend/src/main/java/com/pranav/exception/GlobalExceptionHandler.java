package com.pranav.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Resource Not Found exception
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ExceptionResponse> userNotFound(ResourceNotFound request)
    {
        ExceptionResponse response = ExceptionResponse.builder().message(request.getMessage()).status(HttpStatus.NOT_FOUND).date(new Date()).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }


}
