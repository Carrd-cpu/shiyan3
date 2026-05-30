package com.shiyan3.servlet;

import com.shiyan3.service.FavoriteService;
import com.shiyan3.util.ApiResponse;
import com.shiyan3.util.JsonUtil;
import com.shiyan3.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/favorites/*")
public class FavoriteServlet extends HttpServlet {
    private final FavoriteService favoriteService = new FavoriteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = SessionUtil.getUserId(req);
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            JsonUtil.writeJson(resp, ApiResponse.success(favoriteService.listFavorites(userId)));
            return;
        }
        if (path.matches("/check/\\d+")) {
            int houseId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            JsonUtil.writeJson(resp, ApiResponse.success(Map.of("favorite", favoriteService.isFavorite(userId, houseId))));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = SessionUtil.getUserId(req);
        String path = req.getPathInfo();
        if (path != null && path.matches("/\\d+")) {
            int houseId = Integer.parseInt(path.substring(1));
            if (favoriteService.addFavorite(userId, houseId)) {
                JsonUtil.writeJson(resp, ApiResponse.success(null));
            } else {
                JsonUtil.writeJson(resp, ApiResponse.error(400, "收藏失败，仅可收藏已发布房源"));
            }
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer userId = SessionUtil.getUserId(req);
        String path = req.getPathInfo();
        if (path != null && path.matches("/\\d+")) {
            int houseId = Integer.parseInt(path.substring(1));
            favoriteService.removeFavorite(userId, houseId);
            JsonUtil.writeJson(resp, ApiResponse.success(null));
            return;
        }
        JsonUtil.writeJson(resp, ApiResponse.error(404, "接口不存在"));
    }
}
