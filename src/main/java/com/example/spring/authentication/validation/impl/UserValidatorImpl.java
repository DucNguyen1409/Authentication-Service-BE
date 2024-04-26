package com.example.spring.authentication.validation.impl;

import com.example.spring.authentication.exception.AuthenticationException;
import com.example.spring.authentication.service.UserService;
import com.example.spring.authentication.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {

    private final UserService userService;

    @Override
    public void checkUserExist(String id) {
        boolean existById = userService.existsById(id);
        if (!existById) {
            throw new AuthenticationException("NOT_EXISTS_USER", "The user ID is not exist");
        }
    }
}
