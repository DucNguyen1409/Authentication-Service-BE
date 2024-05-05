package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.dto.ActivateAccountDto;
import com.example.spring.authentication.dto.AuthenticationRequestDto;
import com.example.spring.authentication.dto.AuthenticationResponseDto;
import com.example.spring.authentication.dto.CustomerDto;
import com.example.spring.authentication.entity.Role;
import com.example.spring.authentication.entity.Token;
import com.example.spring.authentication.entity.TokenType;
import com.example.spring.authentication.entity.User;
import com.example.spring.authentication.exception.ActivateTokenExpiredException;
import com.example.spring.authentication.exception.UnauthenticatedException;
import com.example.spring.authentication.rabbitmq.Producer;
import com.example.spring.authentication.repository.RoleRepository;
import com.example.spring.authentication.service.*;
import com.example.spring.authentication.utils.ActivationCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    private static final int EXPIRES_MINUTE = 15;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserService userService;

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final Producer producer;

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto requestDto) {
        // authenticate user request
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );

        // find user token before generate new one.
        User user = userService.findByEmail(requestDto.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());

        // revoke user token
        revokeAllUserToken(user);

        // generate new token
        String accessToken = jwtService.generateToken(claims, user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save generated token
        saveUserToken(user, accessToken);
        return AuthenticationResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(AuthenticationRequestDto requestDto) {
        User userExist = userService.findByEmail(requestDto.getEmail());
        if (Objects.nonNull(userExist.getId())) {
            throw new UnauthenticatedException("user email exist");
        }

        // get role
        Role userRole = roleRepository.findByName(requestDto.getRole())
                .orElseThrow(() -> new UnauthenticatedException("Role not found: " + requestDto.getRole()));

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .accountLocked(false)
                .enable(false)
                .roles(List.of(userRole))
                .build();

        // save user
        userService.saveAndGetObject(user);

        // send validate account email
        sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        // check valid jwt token
        if (Objects.isNull(authHeader) || !authHeader.startsWith(Constant.BEARER)) {
            throw new UnauthenticatedException("Authorization failed");
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

                return AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

            }
        }
        throw new UnauthenticatedException("Authorization failed");
    }

    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRES_MINUTE))
                .expired(false)
                .revoked(false)
                .build();

        tokenService.save(token);
    }

    @Override
//    @Transactional
    public void activateAccount(String token) {
        Token savedToken = tokenService.findByToken(token);
        if (Objects.isNull(savedToken.getId())) {
            throw new UnauthenticatedException("Invalid token");
        }

        if (Objects.nonNull(savedToken.getValidatedAt())) {
            throw new UnauthenticatedException("Token already validate");
        }

        // valid token is expired
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new ActivateTokenExpiredException("Activation token has expired. New token has been sent");
        }

        // valid user in system
        User user = userService.findById(savedToken.getUser().getId());
        if (Objects.isNull(user.getId())) {
            throw new UnauthenticatedException("User name not found");
        }

        // enable user account
        user.setEnable(true);
        userService.saveAndGetObject(user);

        // update token validation time
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenService.save(savedToken);

        // send mail register successfully
        String roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(", "));
        CustomerDto customerDto = CustomerDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(roles)
                .build();
        producer.send(customerDto);
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

    private void sendValidationEmail(User user) {
        String tokenCode = generateAndSaveActivationToken(user);
        // send activate email
        ActivateAccountDto accountDto = ActivateAccountDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .code(tokenCode)
                .confirmUrl(activationUrl)
                .build();
        producer.send(accountDto);
    }

    private String generateAndSaveActivationToken(User user) {
        // generate token
        String generatedToken = ActivationCodeUtils.generateActivationCode(4);
        // save token
        saveUserToken(user, generatedToken);
        return generatedToken;
    }

}
