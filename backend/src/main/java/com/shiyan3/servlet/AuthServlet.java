package com.shiyan3.servlet;

import com.shiyan3.service.AuthService;
import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.ServletUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/login".equals(path)) {
            Map<String, Object> body = ServletUtil.readBodyAsMap(req);
            String username = body.getOrDefault("username", "").toString();
            String password = body.getOrDefault("password", "").toString();
            Map<String, Object> result = authService.login(username, password);
            Integer code = (Integer) result.get("code");
            if (code != null && code == 0) {
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                HttpSession session = req.getSession(true);
                session.setAttribute(SessionUtil.USER_ID, data.get("id"));
                session.setAttribute(SessionUtil.USER_ROLE, data.get("role"));
                session.setAttribute(SessionUtil.USERNAME, data.get("username"));
                JsonUtil.writeJson(resp, ApiResponse.success(data));
                return;
            }
            JsonUtil.writeJson(resp, ApiResponse.error(code == null ? 500 : code, result.get("message").toString()));
            return;
        }

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            JsonUtil.writeJson(resp, ApiResponse.success(null));
            return;
        }

        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if ("/me".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session == null) {
                JsonUtil.writeJson(resp, ApiResponse.error(401, "未登录"));
                return;
            }
            JsonUtil.writeJson(resp, ApiResponse.success(Map.of(
                    "id", session.getAttribute(SessionUtil.USER_ID),
                    "role", session.getAttribute(SessionUtil.USER_ROLE),
                    "username", session.getAttribute(SessionUtil.USERNAME)
            )));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }
}
