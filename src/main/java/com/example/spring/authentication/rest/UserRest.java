package com.example.spring.authentication.rest;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("[UserRest] findAll");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/get")
    public String getResouseUser() {
        return "GET:: users";
    }
}
