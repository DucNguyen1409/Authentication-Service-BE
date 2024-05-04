package com.example.spring.authentication.repository;

import com.example.spring.authentication.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, String> {
}
