package com.example.spring.authentication.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUserName(final String token);

    <T> T extractClaim(final String token, Function<Claims, T> claimsTFunction);

    Claims extractAllClaims(final String token);

    String generateToken(final UserDetails userDetails);

    String generateToken(final Map<String, Object> extraClaims, final UserDetails userDetails);

    String generateRefreshToken(final UserDetails userDetails);

    boolean isTokenValid(final String token, final UserDetails userDetails);

}
