package com.example.spring.authentication.repository;

import com.example.spring.authentication.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {

    @Query("FROM Token t JOIN User u ON t.user.id = u.id"
            + " WHERE u.id = :userId "
            + "     AND (t.expired = FALSE or t.revoked = FALSE)")
    List<Token> findAllValidTokenByUserId(final String userId);

    Optional<Token> findByToken(final String token);

}
