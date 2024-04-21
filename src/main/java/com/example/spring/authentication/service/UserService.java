package com.example.spring.authentication.service;

import com.example.spring.authentication.entity.User;

public interface UserService {
    User findByEmail(final String email);

    void saveAndGetObject(final User user);
}
