package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.entity.User;
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
    public User findByEmail(String email) {
        Optional<User> optUser = repo.findByEmail(email);
        return optUser.orElseGet(User::new);
    }

    @Override
    public void saveAndGetObject(User user) {
        repo.save(user);
    }

}
