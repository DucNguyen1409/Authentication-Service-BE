package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(final String email);

    void saveAndGetObject(final User user);

    List<UserDto> findAll();

    User findById(final String id);

    void updateName(final String id, final String name);

    Boolean existsById(final String id);

    User getCurrentUserInfo();

}
