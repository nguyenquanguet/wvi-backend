package com.vnmo.backend.respository;

import com.vnmo.backend.dto.LoginResponse;
import com.vnmo.backend.mapper.AuthenticationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthenticationRepository {
    private final AuthenticationMapper authenticationMapper;

    public int existedUsernameByUsername(String username){
        return authenticationMapper.existedUsernameByUsername(username);
    }

    public int existedUsernameAndPassword(String username, String password){
        return authenticationMapper.existedUsernameAndPassword(username, password);
    }

    public LoginResponse findUser(String username, String password) {
        return authenticationMapper.findUser(username, password);
    }

    public Integer findApIdUserByUsername(String username) {
        return authenticationMapper.findApIdUserByUsername(username);
    }
}
