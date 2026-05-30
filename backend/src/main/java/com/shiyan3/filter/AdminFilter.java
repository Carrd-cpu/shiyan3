package com.shiyan3.filter;

import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (!"admin".equals(SessionUtil.getRole(req))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            JsonUtil.writeJson(resp, ApiResponse.error(403, "无管理员权限"));
            return;
        }
        chain.doFilter(request, response);
    }
}
