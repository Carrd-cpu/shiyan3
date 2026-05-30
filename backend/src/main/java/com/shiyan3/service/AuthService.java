package com.shiyan3.service;

import com.shiyan3.dao.UserDao;
import com.shiyan3.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final int MAX_FAILS = 5;
    private static final int LOCK_MINUTES = 10;
    private final UserDao userDao = new UserDao();

    public Map<String, Object> login(String username, String password) {
        User user = userDao.findByUsername(username);
        Map<String, Object> result = new HashMap<>();
        if (user == null) {
            result.put("code", 4001);
            result.put("message", "账号或密码错误");
            return result;
        }

        if (user.getEnabled() != null && user.getEnabled() == 0) {
            result.put("code", 4003);
            result.put("message", "账号已被禁用");
            return result;
        }

        LocalDateTime now = LocalDateTime.now();
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(now)) {
            result.put("code", 4002);
            result.put("message", "账号已锁定，" + user.getLockUntil() + " 后可再试");
            return result;
        }

        if (user.getPassword() == null || !user.getPassword().equals(password)) {
            int failed = (user.getFailedAttempts() == null ? 0 : user.getFailedAttempts()) + 1;
            Timestamp lockUntil = null;
            if (failed >= MAX_FAILS) {
                lockUntil = Timestamp.valueOf(now.plusMinutes(LOCK_MINUTES));
            }
            userDao.updateLoginFailure(user.getId(), failed, lockUntil);
            result.put("code", 4001);
            result.put("message", lockUntil == null ? "账号或密码错误" : "失败次数过多，账号已锁定10分钟");
            return result;
        }

        userDao.resetLoginFailures(user.getId());
        result.put("code", 0);
        result.put("message", "success");
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("nickname", user.getNickname());
        result.put("data", data);
        return result;
    }
}
