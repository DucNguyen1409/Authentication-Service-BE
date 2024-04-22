package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.AuthenticationRequestDto;
import com.example.spring.authentication.dto.AuthenticationResponseDto;
import com.example.spring.authentication.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto);

    AuthenticationResponseDto register(AuthenticationRequestDto requestDto) throws MessagingException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void saveUserToken(final User user, final String jwtToken);
}
