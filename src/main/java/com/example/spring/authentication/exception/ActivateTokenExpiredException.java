package com.example.spring.authentication.exception;

public class ActivateTokenExpiredException extends RuntimeException {
    public ActivateTokenExpiredException(String msg) {
        super(msg);
    }
}
