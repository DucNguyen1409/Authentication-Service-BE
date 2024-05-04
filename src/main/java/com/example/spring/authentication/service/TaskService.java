package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.TaskDto;
import com.example.spring.authentication.dto.TaskRequestDto;

import java.util.List;

public interface TaskService {

    String save(final TaskRequestDto dto);

    List<TaskDto> findByUserId(final String userId);

    List<TaskDto> findAll();

}
