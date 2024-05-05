package com.example.spring.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateAccountDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty
    private String code;

    @JsonProperty
    private String confirmUrl;
}
