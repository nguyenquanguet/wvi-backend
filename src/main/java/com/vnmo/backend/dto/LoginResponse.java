package com.vnmo.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Integer userId;

    private String username;

    private String email;

    private Integer userApId;

    private String userApName;

    private Integer inputTarget;

    private Integer year;
}
