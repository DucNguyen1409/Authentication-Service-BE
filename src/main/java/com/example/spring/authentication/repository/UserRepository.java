package com.example.spring.authentication.repository;

import com.example.spring.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(final String email);

    boolean existsById(final String id);

}
