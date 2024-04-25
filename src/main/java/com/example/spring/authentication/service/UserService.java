package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(final String email);

    void saveAndGetObject(final User user);

    List<UserDto> findAll();

}
