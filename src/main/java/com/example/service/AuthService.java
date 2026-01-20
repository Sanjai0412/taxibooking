package com.example.service;

import java.sql.SQLException;

import com.example.dao.AuthDao;
import com.example.dto.SignUpRequestDto;
import com.example.dto.UserDto;
import com.example.models.User;

public class AuthService {

    AuthDao authDao = new AuthDao();

    public UserDto authenticateUser(String email, String password) throws SQLException {
        User user = authDao.authenticate(email, password);
        return com.example.mapper.UserMapper.toDto(user);
    }

    public User registerUser(SignUpRequestDto signUpRequestDto) throws SQLException {
        return authDao.createUser(signUpRequestDto);
    }
}
