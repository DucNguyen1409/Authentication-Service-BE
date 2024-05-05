package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.entity.User;
import com.example.spring.authentication.exception.UserNotFoundException;
import com.example.spring.authentication.mapper.ObjectMapperUtils;
import com.example.spring.authentication.repository.UserRepository;
import com.example.spring.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public void saveAndGetObject(User user) {
        repo.save(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> entities = repo.findAll();
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }

        return ObjectMapperUtils.mapAll(entities, UserDto.class);
    }

    @Override
    public User findById(String id) {
        Optional<User> optUser = repo.findById(id);
        return optUser.orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Override
    public void updateName(String id, String name) {
        User user = findById(id);
        user.setName(name);

        // save user
        repo.save(user);
    }

    @Override
    public Boolean existsById(String id) {
        return repo.existsById(id);
    }

    @Override
    public User getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optUser = repo.findByName(authentication.getName());

        return optUser.orElseGet(User::new);
    }

}
