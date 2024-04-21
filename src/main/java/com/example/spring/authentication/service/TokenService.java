package com.example.spring.authentication.service;

import com.example.spring.authentication.entity.Token;

import java.util.List;

public interface TokenService {

    void save(final Token token);

    List<Token> findAllValidTokenByUserId(final String userId);

    Token findByToken(final String token);

    void saveAll(final List<Token> tokens);
}
