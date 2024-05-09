package com.example.spring.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class AuthenticationRequestDto {

    @NotEmpty(message = "Name cannot empty")
    @NotNull(message = "Name cannot empty")
    private String name;

    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email cannot empty")
    @NotNull(message = "Email cannot empty")
    private String email;

    @NotEmpty(message = "Password cannot empty")
    @NotNull(message = "Password cannot empty")
    private String password;

    @NotNull(message = "Role cannot empty")
    @NotEmpty(message = "Role cannot empty")
    private String role;

}
