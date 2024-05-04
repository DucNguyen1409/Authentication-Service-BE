package com.example.spring.authentication.repository;

import com.example.spring.authentication.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("FROM Task t "
            + " WHERE t.assignee = :userId")
    List<Task> findByUserId(final String userId);

}
