package com.shiyan3.servlet;

import com.shiyan3.entity.House;
import com.shiyan3.service.HouseService;
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

@WebServlet("/api/houses/*")
public class HouseServlet extends HttpServlet {
    private final HouseService houseService = new HouseService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            int page = Math.max(1, ServletUtil.getIntParam(req, "page", 1));
            int size = Math.max(1, ServletUtil.getIntParam(req, "size", 10));
            Map<String, Object> data = houseService.listApproved(
                    req.getParameter("keyword"),
                    req.getParameter("region"),
                    req.getParameter("layout"),
                    ServletUtil.getDoubleParam(req, "minPrice"),
                    ServletUtil.getDoubleParam(req, "maxPrice"),
                    page,
                    size
            );
            JsonUtil.writeJson(resp, ApiResponse.success(data));
            return;
        }

        if (path.matches("/\\d+")) {
            int id = Integer.parseInt(path.substring(1));
            House house = houseService.getById(id);
            if (house == null) {
                JsonUtil.writeJson(resp, ApiResponse.error(404, "房源不存在"));
                return;
            }
            Integer userId = SessionUtil.getUserId(req);
            String role = SessionUtil.getRole(req);
            if (!"APPROVED".equals(house.getStatus()) && !(userId != null && "admin".equals(role))) {
                JsonUtil.writeJson(resp, ApiResponse.error(403, "该房源暂不可见"));
                return;
            }
            JsonUtil.writeJson(resp, ApiResponse.success(house));
            return;
        }

        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }
}
