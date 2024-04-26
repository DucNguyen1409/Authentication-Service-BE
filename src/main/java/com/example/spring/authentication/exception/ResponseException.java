package com.example.spring.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseException {
    private String path;
    private String code;
    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;
}
