package com.vnmo.backend.mapper;

import com.vnmo.backend.dto.LoginResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthenticationMapper {

    @Select("SELECT EXISTS(SELECT 1 FROM user WHERE username = #{username})")
    int existedUsernameByUsername(String username);

    @Select("SELECT EXISTS(SELECT 1 FROM user WHERE username = #{username} AND password = #{password})")
    int existedUsernameAndPassword(String username, String password);

    LoginResponse findUser(String username, String password);

    Integer findApIdUserByUsername(String username);
}
