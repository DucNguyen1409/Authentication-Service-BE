package com.example.spring.authentication.exception;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {AuthenException.class})
    public ResponseEntity<?> handleApiRequestException(AuthenException ex) {
        // 1. Create payload containing exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ResponseException apiException = new ResponseException(
                ex.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        // 2. Return response entity
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {MessagingException.class})
    public ResponseEntity<?> handleMessagingException(MessagingException ex) {
        HttpStatus badGateway = HttpStatus.BAD_GATEWAY;

        ResponseException exception = new ResponseException(
                ex.getMessage(), badGateway, ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, badGateway);
    }
}
