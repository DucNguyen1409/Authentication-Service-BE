package com.example.spring.authentication.dto;

import com.example.spring.authentication.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    private String name;
    private String email;
    private String passwd;
    private Role role;
}
