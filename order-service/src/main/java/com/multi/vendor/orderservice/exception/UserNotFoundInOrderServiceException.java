package com.multi.vendor.orderservice.exception;

public class UserNotFoundInOrderServiceException extends RuntimeException {
    public UserNotFoundInOrderServiceException(String message) {
        super(message);
    }
}
