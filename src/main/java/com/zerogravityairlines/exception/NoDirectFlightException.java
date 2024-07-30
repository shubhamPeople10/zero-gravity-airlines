package com.zerogravityairlines.exception;

public class NoDirectFlightException extends RuntimeException {
    public NoDirectFlightException(String message) {
        super(message);
    }
}