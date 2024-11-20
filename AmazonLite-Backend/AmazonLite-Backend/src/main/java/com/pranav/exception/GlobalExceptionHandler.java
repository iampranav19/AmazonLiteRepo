package com.pranav.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Resource Not Found exception
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ExceptionResponse> userNotFound(ResourceNotFound request)
    {
        ExceptionResponse response = ExceptionResponse.builder().message(request.getMessage()).status(HttpStatus.NOT_FOUND).date(new Date()).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    // MethodArgumentException exception

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e)
    {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, Object> errors = new HashMap<>();
        allErrors.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}
