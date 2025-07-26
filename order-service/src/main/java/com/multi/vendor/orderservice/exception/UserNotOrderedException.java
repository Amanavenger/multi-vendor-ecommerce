package com.multi.vendor.orderservice.exception;

public class UserNotOrderedException extends RuntimeException {
    public UserNotOrderedException(String message) {
        super(message);
    }
}
