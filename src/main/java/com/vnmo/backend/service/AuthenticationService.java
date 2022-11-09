package com.vnmo.backend.service;

import com.vnmo.backend.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> logout();
}
