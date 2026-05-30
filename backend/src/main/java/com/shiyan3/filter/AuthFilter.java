package com.shiyan3.filter;

import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        boolean pass = uri.endsWith("/api/auth/login")
                || uri.endsWith("/api/houses")
                || uri.matches(".*/api/houses/\\d+$");

        if (pass) {
            chain.doFilter(request, response);
            return;
        }

        if (SessionUtil.getUserId(req) == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonUtil.writeJson(resp, ApiResponse.error(401, "请先登录"));
            return;
        }
        chain.doFilter(request, response);
    }
}
