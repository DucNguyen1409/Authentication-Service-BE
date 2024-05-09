package com.example.spring.authentication.rest;

import com.example.spring.authentication.dto.AuthenticationRequestDto;
import com.example.spring.authentication.dto.AuthenticationResponseDto;
import com.example.spring.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRest {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationRequestDto requestDto) {
        authenticationService.register(requestDto);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto requestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(requestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) {
        authenticationService.activateAccount(token);
    }
}
