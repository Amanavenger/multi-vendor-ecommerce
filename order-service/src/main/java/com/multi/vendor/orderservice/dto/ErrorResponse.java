package com.multi.vendor.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String message;

    private int status;

    private LocalDateTime timestamp;
}
