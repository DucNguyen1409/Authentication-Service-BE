package com.example.spring.authentication.exception;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseException> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                                  WebRequest webRequest) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            var errorMsg = error.getDefaultMessage();
            errors.add(errorMsg);
        });

        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                errors, null);
        log.error(String.valueOf(responseException), exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseException);
    }

    @ExceptionHandler(value = {ActivateTokenExpiredException.class})
    public ResponseEntity<ResponseException> handleActivateTokenExpired(ActivateTokenExpiredException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.UNAUTHORIZED,
                Collections.singleton(exception.getMessage()), null);
        log.error(String.valueOf(responseException), exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseException);
    }

    @ExceptionHandler(value = {DisabledException.class})
    public ResponseEntity<ResponseException> handleDisabledException(DisabledException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                Collections.singleton(exception.getMessage()), null);
        log.error(String.valueOf(responseException), exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }

    @ExceptionHandler(value = {UnauthenticatedException.class})
    public ResponseEntity<ResponseException> handleUnauthenticatedException(UnauthenticatedException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.UNAUTHORIZED,
                Collections.singleton(exception.getMessage()), null);
        log.error(String.valueOf(responseException), exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseException);
    }

    @ExceptionHandler(value = {MessagingException.class})
    public ResponseEntity<ResponseException> handleMessagingException(MessagingException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singleton("Internal error, contact the Admin"), null);
        exception.printStackTrace();
        log.error(String.valueOf(responseException), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseException> handleException(final Exception exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.INTERNAL_SERVER_ERROR,
                Collections.singleton("Internal error, contact the Admin"), null);
        exception.printStackTrace();
        log.error(String.valueOf(responseException), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseException);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseException> handleUsernameNotFoundException(final UsernameNotFoundException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                Collections.singleton(exception.getMessage()), null);
        log.error(String.valueOf(responseException), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseException> handleUserNotFoundException(final UserNotFoundException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                Collections.singleton(exception.getMessage()), null);
        log.error(String.valueOf(responseException), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseException> handleAuthenticationException(final AuthenticationException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                Collections.singleton(exception.getMessage()), exception.getCode());
        log.error(String.valueOf(responseException), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }

    private ResponseException buildResponseException(WebRequest webRequest, HttpStatus httpStatus,
                                                     Set<String> message, String code) {
        return ResponseException.builder()
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .localDateTime(LocalDateTime.now())
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .build();
    }
}
