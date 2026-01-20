package com.example.service;

import java.sql.SQLException;

import com.example.dao.UserDao;
import com.example.enums.UserStatus;
import com.example.models.User;

public class UserService {
    UserDao userDao = new UserDao();

    public User getUserById(int userId) throws SQLException {
        return userDao.getUserById(userId);
    }

    public void changeUserStatus(UserStatus status, int userId) throws java.sql.SQLException {
        userDao.changeUserStatus(status, userId);
    }
}
