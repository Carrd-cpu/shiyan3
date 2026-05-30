package com.shiyan3.dao;

import com.shiyan3.entity.House;
import com.shiyan3.util.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {
    public boolean exists(int userId, int houseId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id=? AND house_id=? LIMIT 1";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, houseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int add(int userId, int houseId) {
        String sql = "INSERT INTO favorites(user_id, house_id) VALUES(?,?)";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, houseId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int remove(int userId, int houseId) {
        String sql = "DELETE FROM favorites WHERE user_id=? AND house_id=?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, houseId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<House> listByUserId(int userId) {
        String sql = "SELECT h.* FROM favorites f JOIN houses h ON f.house_id=h.id WHERE f.user_id=? AND h.status='APPROVED' ORDER BY f.id DESC";
        List<House> list = new ArrayList<>();
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    House house = new House();
                    house.setId(rs.getInt("id"));
                    house.setTitle(rs.getString("title"));
                    house.setCommunity(rs.getString("community"));
                    house.setAddress(rs.getString("address"));
                    house.setRegion(rs.getString("region"));
                    house.setLayout(rs.getString("layout"));
                    house.setArea(rs.getBigDecimal("area"));
                    house.setPrice(rs.getBigDecimal("price"));
                    house.setImageUrl(rs.getString("image_url"));
                    house.setDescription(rs.getString("description"));
                    house.setStatus(rs.getString("status"));
                    house.setRejectReason(rs.getString("reject_reason"));
                    list.add(house);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
