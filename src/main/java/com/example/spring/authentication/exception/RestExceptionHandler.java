package com.example.spring.authentication.exception;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseException handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                   WebRequest webRequest) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            var errorMsg  = error.getDefaultMessage();
            errors.add(errorMsg);
        });

        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                null, null);
        responseException.setValidationErrors(errors);
        log.error(String.valueOf(responseException), exception);

        return responseException;
    }

    @ExceptionHandler(value = {ActivateTokenExpiredException.class})
    public ResponseException handleActivateTokenExpired(ActivateTokenExpiredException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.UNAUTHORIZED,
                exception.getMessage(), null);
        log.error(String.valueOf(responseException), exception);

        return responseException;
    }

    @ExceptionHandler(value = {DisabledException.class})
    public ResponseException handleDisabledException(DisabledException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                exception.getMessage(), null);
        log.error(String.valueOf(responseException), exception);

        return responseException;
    }

    @ExceptionHandler(value = {UnauthenticatedException.class})
    public ResponseException handleUnauthenticatedException(UnauthenticatedException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.UNAUTHORIZED,
                exception.getMessage(), null);
        log.error(String.valueOf(responseException), exception);

        return responseException;
    }

    @ExceptionHandler(value = {MessagingException.class})
    public ResponseException handleMessagingException(MessagingException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal error, contact the Admin", null);
        exception.printStackTrace();
        log.error(String.valueOf(responseException), exception);
        return responseException;
    }

    @ExceptionHandler(Exception.class)
    public ResponseException handleException(final Exception exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal error, contact the Admin", null);
        exception.printStackTrace();
        log.error(String.valueOf(responseException), exception);
        return responseException;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseException handleUsernameNotFoundException(final UsernameNotFoundException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                exception.getMessage(), null);
        log.error(String.valueOf(responseException), exception);
        return responseException;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseException handleUserNotFoundException(final UserNotFoundException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                exception.getMessage(), null);
        log.error(String.valueOf(responseException), exception);
        return responseException;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseException handleAuthenticationException(final AuthenticationException exception, WebRequest webRequest) {
        final ResponseException responseException = buildResponseException(webRequest, HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getCode());
        log.error(String.valueOf(responseException), exception);
        return responseException;
    }

    private ResponseException buildResponseException(WebRequest webRequest, HttpStatus httpStatus,
                                                     String message, String code) {
        return ResponseException.builder()
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .localDateTime(LocalDateTime.now())
                .httpStatus(httpStatus)
                .message(message)
                .code(code)
                .build();
    }
}
