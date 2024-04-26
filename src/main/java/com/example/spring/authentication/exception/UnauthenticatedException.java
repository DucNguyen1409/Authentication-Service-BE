package com.example.spring.authentication.exception;

public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException(final String msg) {
        super(msg);
    }
}
