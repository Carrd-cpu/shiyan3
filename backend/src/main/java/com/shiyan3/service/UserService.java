package com.shiyan3.service;

import com.shiyan3.dao.UserDao;
import com.shiyan3.entity.User;

public class UserService {
    private final UserDao userDao = new UserDao();

    public User getById(Integer id) {
        return userDao.findById(id);
    }

    public int updateProfile(Integer id, String nickname, String phone, String password) {
        User user = userDao.findById(id);
        if (user == null) {
            return 0;
        }
        String finalPwd = (password == null || password.isBlank()) ? user.getPassword() : password;
        return userDao.updateProfile(id, nickname, phone, finalPwd);
    }
}
