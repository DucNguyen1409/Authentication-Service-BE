package com.example.spring.authentication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends RuntimeException {

    private final String code;
    private final String message;
    private HttpStatus httpStatus;

    public AuthenticationException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
