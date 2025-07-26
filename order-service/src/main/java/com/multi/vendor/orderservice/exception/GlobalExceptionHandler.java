package com.multi.vendor.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.multi.vendor.orderservice.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<ErrorResponse> invalidOrderIdException(InvalidOrderIdException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundInOrderServiceException.class)
    public ResponseEntity<ErrorResponse> userNotFoundInOrderServiceException(UserNotFoundInOrderServiceException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotOrderedException.class)
    public ResponseEntity<ErrorResponse> UserNotOrderedException(UserNotOrderedException ex) {
            ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
