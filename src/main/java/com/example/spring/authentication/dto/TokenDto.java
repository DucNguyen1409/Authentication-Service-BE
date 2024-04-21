package com.example.spring.authentication.dto;

import com.example.spring.authentication.entity.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String token;
    private TokenType tokenType;
    private Boolean expired;
    private Boolean revoked;
    private UserDto userDto;
}
