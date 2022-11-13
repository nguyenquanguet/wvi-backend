package com.vnmo.backend.service.impl;

import com.vnmo.backend.dto.LoginRequest;
import com.vnmo.backend.dto.LoginResponse;
import com.vnmo.backend.exception.BusinessException;
import com.vnmo.backend.exception.ExceptionCode;
import com.vnmo.backend.respository.AuthenticationRepository;
import com.vnmo.backend.respository.UtilRepository;
import com.vnmo.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.vnmo.backend.core.ResponseObject.response;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        //Check username
        if (authenticationRepository.existedUsernameByUsername(loginRequest.getUsername()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }
        //Check username and password
        if (authenticationRepository.existedUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword()) != 1) {
            throw new BusinessException(ExceptionCode.ERROR_WRONG_PASSWORD);
        }

        //to Lower Case username
        String username = loginRequest.getUsername();
        loginRequest.setUsername(username.toLowerCase());

        Optional<LoginResponse> loginResponse = authenticationRepository.findUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (loginResponse.isEmpty()) {
            throw new BusinessException(ExceptionCode.ERROR_USERNAME_NOT_FOUND);
        }

        return response(loginResponse.get());
    }

    @Override
    public ResponseEntity<?> logout() {
        return null;
    }
}
