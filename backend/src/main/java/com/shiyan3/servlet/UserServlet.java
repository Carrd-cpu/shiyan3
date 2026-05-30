package com.shiyan3.servlet;

import com.shiyan3.entity.User;
import com.shiyan3.service.UserService;
import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.ServletUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/profile".equals(path)) {
            Integer userId = SessionUtil.getUserId(req);
            User user = userService.getById(userId);
            if (user == null) {
                JsonUtil.writeJson(resp, ApiResponse.error(404, "用户不存在"));
                return;
            }
            user.setPassword(null);
            JsonUtil.writeJson(resp, ApiResponse.success(user));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/profile".equals(path)) {
            Integer userId = SessionUtil.getUserId(req);
            Map<String, Object> body = ServletUtil.readBodyAsMap(req);
            String nickname = body.getOrDefault("nickname", "").toString();
            String phone = body.getOrDefault("phone", "").toString();
            String password = body.getOrDefault("password", "").toString();
            int updated = userService.updateProfile(userId, nickname, phone, password);
            if (updated > 0) {
                JsonUtil.writeJson(resp, ApiResponse.success(null));
                return;
            }
            JsonUtil.writeJson(resp, ApiResponse.error(400, "更新失败"));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }
}
