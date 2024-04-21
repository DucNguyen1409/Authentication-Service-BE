package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.dto.AuthenticationRequestDto;
import com.example.spring.authentication.dto.AuthenticationResponseDto;
import com.example.spring.authentication.dto.TokenDto;
import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.entity.Token;
import com.example.spring.authentication.entity.TokenType;
import com.example.spring.authentication.entity.User;
import com.example.spring.authentication.exception.AuthenException;
import com.example.spring.authentication.mapper.ObjectMapperUtils;
import com.example.spring.authentication.service.AuthenticationService;
import com.example.spring.authentication.service.JwtService;
import com.example.spring.authentication.service.TokenService;
import com.example.spring.authentication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto) {
        // authenticate user request
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPasswd())
        );

        // find user token before generate new one.
        User user = userService.findByEmail(requestDto.getEmail());

        // revoke user token
        revokeAllUserToken(user);

        // generate new token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save generated token
        saveUserToken(user, accessToken);
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto register(AuthenticationRequestDto requestDto) {
        User userExist = userService.findByEmail(requestDto.getEmail());
        if (Objects.nonNull(userExist.getId())) {
            throw new AuthenException("user email exist");
        }

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .passwd(passwordEncoder.encode(requestDto.getPasswd()))
                .role(requestDto.getRole())
                .build();

        // save user
        userService.saveAndGetObject(user);

        // generate token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save token
        saveUserToken(user, accessToken);
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        // check valid jwt token
        if (Objects.isNull(authHeader) || !authHeader.startsWith(Constant.BEARER)) {
            return;
        }

        // extract token
        refreshToken = authHeader.substring(Constant.JWT_BEARER_INDEX);
        userEmail = jwtService.extractUserName(refreshToken);

        /* check user exist on DB
            no need to check user authentication again when get refresh token
            but get user from DB by using findByEmail */
        if (Objects.nonNull(userEmail)) {
            User user = userService.findByEmail(userEmail);

            if (Objects.isNull(user.getId())) {
                throw new UsernameNotFoundException("user not found");
            }

            boolean isValidToken = jwtService.isTokenValid(refreshToken, user);
            if (isValidToken) {
                String accessToken = jwtService.generateToken(user);

                // revoke all access token currently available
                revokeAllUserToken(user);
                saveUserToken(user, accessToken);

                AuthenticationResponseDto authenResponseDto = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                // write body of the response
                new ObjectMapper().writeValue(response.getOutputStream(), authenResponseDto);
            }

        }
    }

    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenService.save(token);
    }

    private void revokeAllUserToken(User user) {
        List<Token> validUserTokens = tokenService.findAllValidTokenByUserId(user.getId());

        if (CollectionUtils.isEmpty(validUserTokens)) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenService.saveAll(validUserTokens);
    }

}
