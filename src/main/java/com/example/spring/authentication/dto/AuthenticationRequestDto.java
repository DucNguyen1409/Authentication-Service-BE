package com.example.spring.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequestDto {

    @Email(message = "Email is not well formatted")
    @NotEmpty(message = "Email cannot empty")
    @NotNull(message = "Email cannot empty")
    private String email;

    @NotEmpty(message = "Password cannot empty")
    @NotNull(message = "Password cannot empty")
    private String password;

}
