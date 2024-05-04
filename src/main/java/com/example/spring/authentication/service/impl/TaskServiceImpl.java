package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.dto.TaskDto;
import com.example.spring.authentication.dto.TaskRequestDto;
import com.example.spring.authentication.entity.Task;
import com.example.spring.authentication.entity.UserTask;
import com.example.spring.authentication.mapper.ObjectMapperUtils;
import com.example.spring.authentication.repository.TaskRepository;
import com.example.spring.authentication.repository.UserTaskRepository;
import com.example.spring.authentication.service.TaskService;
import com.example.spring.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repo;

    private final UserTaskRepository userTaskRepository;

    private final UserService userService;

    @Override
    public String save(TaskRequestDto dto) {
        // save task
        Task task = setDataToTask(dto);
        Task savedTask = repo.save(task);

        // save user task
        UserTask userTask = setDataToUserTask(savedTask, dto.getUserId());
        userTaskRepository.save(userTask);

        return savedTask.getId();
    }

    @Override
    public List<TaskDto> findByUserId(String userId) {
        List<Task> results = repo.findByUserId(userId);
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        return ObjectMapperUtils.mapAll(results, TaskDto.class);
    }

    @Override
    public List<TaskDto> findAll() {
        List<Task> results = repo.findAll();
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        return ObjectMapperUtils.mapAll(results, TaskDto.class);
    }

    private Task setDataToTask(TaskRequestDto dto) {
        String createBy = userService.getCurrentUserInfo().getId();
        return Task.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .assignee(dto.getUserId())
                .createDate(new Date())
                .createBy(Objects.isNull(createBy) ? null : createBy)
                .status("New")
                .build();
    }

    private UserTask setDataToUserTask(Task task, String userId) {
        return UserTask.builder()
                .task(task)
                .userId(userId)
                .build();
    }

}
