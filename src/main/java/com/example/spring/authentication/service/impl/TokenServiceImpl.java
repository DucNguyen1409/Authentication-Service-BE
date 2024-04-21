package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.dto.TokenDto;
import com.example.spring.authentication.entity.Token;
import com.example.spring.authentication.mapper.ObjectMapperUtils;
import com.example.spring.authentication.repository.TokenRepository;
import com.example.spring.authentication.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository repo;

    @Override
    @Transactional
    public void save(Token token) {
        repo.save(token);
    }

    @Override
    public List<TokenDto> findAllValidTokenByUserId(String userId) {
        List<Token> entities = repo.findAllValidTokenByUserId(userId);
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }

        return ObjectMapperUtils.mapAll(entities, TokenDto.class);
    }

    @Override
    public TokenDto findByToken(String token) {
        Optional<Token> optToken = repo.findByToken(token);
        if (optToken.isEmpty()) {
            return new TokenDto();
        }


        return ObjectMapperUtils.map(optToken.get(), TokenDto.class);
    }

    @Override
    @Transactional
    public void saveAll(List<Token> tokens) {
        repo.saveAll(tokens);
    }
}
