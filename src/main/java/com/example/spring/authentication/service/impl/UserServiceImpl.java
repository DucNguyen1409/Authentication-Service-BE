package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.entity.User;
import com.example.spring.authentication.mapper.ObjectMapperUtils;
import com.example.spring.authentication.repository.UserRepository;
import com.example.spring.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public UserDto findByEmail(String email) {
        Optional<User> optUser = repo.findByEmail(email);
        if (optUser.isEmpty()) {
            return new UserDto();
        }

        return ObjectMapperUtils.map(optUser.get(), UserDto.class);
    }

    @Override
    public void saveAndGetObject(User user) {
        repo.save(user);
    }

}
