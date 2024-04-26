package com.example.spring.authentication.validation;

public interface UserValidator {
    void checkUserExist(final String id);
}
