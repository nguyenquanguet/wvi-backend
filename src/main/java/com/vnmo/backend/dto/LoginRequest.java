package com.vnmo.backend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Builder
@Data
public class LoginRequest {

    @NotBlank(message = "Username has blank !")
    private String username;

    @NotBlank(message = "Password has blank !")
    private String password;
}
