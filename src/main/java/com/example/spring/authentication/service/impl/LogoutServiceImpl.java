package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.entity.Token;
import com.example.spring.authentication.service.LogoutService;
import com.example.spring.authentication.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService, LogoutHandler {

    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(Constant.AUTHORIZATION);
        final String jwt;

        // check jwt token
        if (Objects.isNull(authHeader)
                || !authHeader.startsWith(Constant.BEARER)) {
            return;
        }

        // extract token from header
        jwt = authHeader.substring(Constant.JWT_BEARER_INDEX);

        Token storedToken = tokenService.findByToken(jwt);

        if (Objects.nonNull(storedToken.getId())) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            tokenService.save(storedToken);
        }
    }

}
