package com.example.spring.authentication.rest;

import com.example.spring.authentication.dto.TaskDto;
import com.example.spring.authentication.dto.TaskRequestDto;
import com.example.spring.authentication.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskRest {

    private final TaskService taskService;

//    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll() {
        log.info("[TaskRest] findAll");
        List<TaskDto> results = taskService.findAll();

        return ResponseEntity.ok(results);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDto>> findByUserId(@PathVariable String userId) {
        log.info("[TaskRest] findByUserId: {}", userId);
        List<TaskDto> results = taskService.findByUserId(userId);

        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody TaskRequestDto dto) {
        // TODO: validation TaskRequestDto
        log.info("[TaskRest] save: {}", dto);
        String result = taskService.save(dto);

        return ResponseEntity.ok(result);
    }
}
