package com.example.spring.authentication.rest;

import com.example.spring.authentication.dto.UserDto;
import com.example.spring.authentication.service.UserService;
import com.example.spring.authentication.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;

    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("[UserRest] findAll");
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("{id}/name")
    public ResponseEntity<Map<String, String>> updateName(@PathVariable String id, @RequestBody String name) {
        log.info("[UserRest] updateName: {}, {}", id, name);
        // check exit user
        userValidator.checkUserExist(id);

        // update name
        userService.updateName(id, name);
        return ResponseEntity.ok(Collections.singletonMap("Message", "OK"));
    }

}
