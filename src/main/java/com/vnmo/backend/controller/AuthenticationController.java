package com.vnmo.backend.controller;

import com.vnmo.backend.dto.LoginRequest;
import com.vnmo.backend.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public record AuthenticationController(AuthenticationService authenticationService) {

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @CrossOrigin
    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }
}
