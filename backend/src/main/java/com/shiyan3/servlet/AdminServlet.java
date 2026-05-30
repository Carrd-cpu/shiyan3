package com.shiyan3.servlet;

import com.shiyan3.entity.House;
import com.shiyan3.service.AdminService;
import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.ServletUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/api/admin/*")
public class AdminServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        int page = Math.max(1, ServletUtil.getIntParam(req, "page", 1));
        int size = Math.max(1, ServletUtil.getIntParam(req, "size", 10));
        if ("/houses".equals(path)) {
            JsonUtil.writeJson(resp, ApiResponse.success(adminService.listHouses(req.getParameter("keyword"), req.getParameter("status"), page, size)));
            return;
        }
        if ("/reviews".equals(path)) {
            JsonUtil.writeJson(resp, ApiResponse.success(adminService.listPending(page, size)));
            return;
        }
        if ("/users".equals(path)) {
            JsonUtil.writeJson(resp, ApiResponse.success(adminService.listUsers(page, size)));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        Map<String, Object> body = ServletUtil.readBodyAsMap(req);
        if ("/houses".equals(path)) {
            House house = parseHouse(body);
            house.setStatus("PENDING");
            house.setCreatedBy(SessionUtil.getUserId(req));
            int id = adminService.createHouse(house);
            JsonUtil.writeJson(resp, ApiResponse.success(Map.of("id", id)));
            return;
        }

        if (path != null && path.matches("/reviews/\\d+")) {
            int houseId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            String action = body.getOrDefault("action", "").toString();
            String status = "APPROVED".equals(action) ? "APPROVED" : "REJECTED";
            String reason = "REJECTED".equals(status) ? body.getOrDefault("reason", "").toString() : null;
            int updated = adminService.audit(houseId, status, reason);
            if (updated > 0) {
                JsonUtil.writeJson(resp, ApiResponse.success(null));
            } else {
                JsonUtil.writeJson(resp, ApiResponse.error(400, "审核失败，仅允许审核待审核房源"));
            }
            return;
        }

        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        Map<String, Object> body = ServletUtil.readBodyAsMap(req);
        if (path != null && path.matches("/houses/\\d+")) {
            House house = parseHouse(body);
            house.setId(Integer.parseInt(path.substring(path.lastIndexOf('/') + 1)));
            int updated = adminService.updateHouse(house);
            JsonUtil.writeJson(resp, updated > 0 ? ApiResponse.success(null) : ApiResponse.error(400, "更新失败"));
            return;
        }
        if (path != null && path.matches("/users/\\d+/status")) {
            String[] parts = path.split("/");
            int userId = Integer.parseInt(parts[2]);
            int enabled = Integer.parseInt(body.getOrDefault("enabled", 1).toString());
            int updated = adminService.updateUserStatus(userId, enabled);
            JsonUtil.writeJson(resp, updated > 0 ? ApiResponse.success(null) : ApiResponse.error(400, "更新失败"));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path != null && path.matches("/houses/\\d+")) {
            int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            int updated = adminService.deleteHouse(id);
            JsonUtil.writeJson(resp, updated > 0 ? ApiResponse.success(null) : ApiResponse.error(400, "删除失败"));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    private House parseHouse(Map<String, Object> body) {
        House house = new House();
        house.setTitle(body.getOrDefault("title", "").toString());
        house.setCommunity(body.getOrDefault("community", "").toString());
        house.setAddress(body.getOrDefault("address", "").toString());
        house.setRegion(body.getOrDefault("region", "").toString());
        house.setLayout(body.getOrDefault("layout", "").toString());
        house.setArea(new BigDecimal(body.getOrDefault("area", "0").toString()));
        house.setPrice(new BigDecimal(body.getOrDefault("price", "0").toString()));
        house.setImageUrl(body.getOrDefault("imageUrl", "").toString());
        house.setDescription(body.getOrDefault("description", "").toString());
        return house;
    }
}
