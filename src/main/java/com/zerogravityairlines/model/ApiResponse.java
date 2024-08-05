package com.zerogravityairlines.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponse<T> {

    private int statusCode;
    private T data;
    private String error;
    private LocalDateTime timestamp;

    public ApiResponse(int statusCode, T data, String error) {
        this.statusCode = statusCode;
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}