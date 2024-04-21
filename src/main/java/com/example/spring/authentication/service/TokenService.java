package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.TokenDto;
import com.example.spring.authentication.entity.Token;

import java.util.List;

public interface TokenService {

    void save(final Token token);

    List<TokenDto> findAllValidTokenByUserId(final String userId);

    TokenDto findByToken(final String token);

    void saveAll(final List<Token> tokens);
}
