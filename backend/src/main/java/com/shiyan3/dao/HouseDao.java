package com.shiyan3.dao;

import com.shiyan3.entity.House;
import com.shiyan3.util.DruidUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseDao {
    public int create(House house) {
        String sql = "INSERT INTO houses(title, community, address, region, layout, area, price, image_url, description, status, created_by) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillHouseParams(house, ps);
            ps.setString(10, house.getStatus());
            ps.setInt(11, house.getCreatedBy());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int update(House house) {
        String sql = "UPDATE houses SET title=?, community=?, address=?, region=?, layout=?, area=?, price=?, image_url=?, description=?, status='PENDING', reject_reason=NULL WHERE id=?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            fillHouseParams(house, ps);
            ps.setInt(10, house.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int delete(int id) {
        String sql = "DELETE FROM houses WHERE id=?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public House findById(int id) {
        String sql = "SELECT * FROM houses WHERE id=?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapHouse(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<House> listApproved(String keyword, String region, String layout, Double minPrice, Double maxPrice, int offset, int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM houses WHERE status='APPROVED'");
        List<Object> params = new ArrayList<>();
        appendFilter(keyword, region, layout, minPrice, maxPrice, sql, params);
        sql.append(" ORDER BY id DESC LIMIT ?,?");
        params.add(offset);
        params.add(size);
        return queryList(sql.toString(), params);
    }

    public int countApproved(String keyword, String region, String layout, Double minPrice, Double maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM houses WHERE status='APPROVED'");
        List<Object> params = new ArrayList<>();
        appendFilter(keyword, region, layout, minPrice, maxPrice, sql, params);
        return queryCount(sql.toString(), params);
    }

    public List<House> listAdmin(String keyword, String status, int offset, int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM houses WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (title LIKE ? OR community LIKE ? OR address LIKE ?)");
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status=?");
            params.add(status);
        }
        sql.append(" ORDER BY id DESC LIMIT ?,?");
        params.add(offset);
        params.add(size);
        return queryList(sql.toString(), params);
    }

    public int countAdmin(String keyword, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM houses WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (title LIKE ? OR community LIKE ? OR address LIKE ?)");
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
        }
        if (status != null && !status.isBlank()) {
            sql.append(" AND status=?");
            params.add(status);
        }
        return queryCount(sql.toString(), params);
    }

    public List<House> listPending(int offset, int size) {
        String sql = "SELECT * FROM houses WHERE status='PENDING' ORDER BY id DESC LIMIT ?, ?";
        List<Object> params = new ArrayList<>();
        params.add(offset);
        params.add(size);
        return queryList(sql, params);
    }

    public int countPending() {
        return queryCount("SELECT COUNT(*) FROM houses WHERE status='PENDING'", new ArrayList<>());
    }

    public int audit(int houseId, String status, String reason) {
        String sql = "UPDATE houses SET status=?, reject_reason=? WHERE id=? AND status='PENDING'";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, reason);
            ps.setInt(3, houseId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendFilter(String keyword, String region, String layout, Double minPrice, Double maxPrice, StringBuilder sql, List<Object> params) {
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (title LIKE ? OR community LIKE ? OR address LIKE ?)");
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
            params.add('%' + keyword + '%');
        }
        if (region != null && !region.isBlank()) {
            sql.append(" AND region = ?");
            params.add(region);
        }
        if (layout != null && !layout.isBlank()) {
            sql.append(" AND layout = ?");
            params.add(layout);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(BigDecimal.valueOf(minPrice));
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(BigDecimal.valueOf(maxPrice));
        }
    }

    private void fillHouseParams(House house, PreparedStatement ps) throws SQLException {
        ps.setString(1, house.getTitle());
        ps.setString(2, house.getCommunity());
        ps.setString(3, house.getAddress());
        ps.setString(4, house.getRegion());
        ps.setString(5, house.getLayout());
        ps.setBigDecimal(6, house.getArea());
        ps.setBigDecimal(7, house.getPrice());
        ps.setString(8, house.getImageUrl());
        ps.setString(9, house.getDescription());
    }

    private List<House> queryList(String sql, List<Object> params) {
        List<House> list = new ArrayList<>();
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapHouse(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private int queryCount(String sql, List<Object> params) {
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private House mapHouse(ResultSet rs) throws SQLException {
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
        house.setCreatedBy(rs.getInt("created_by"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            house.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            house.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return house;
    }
}
