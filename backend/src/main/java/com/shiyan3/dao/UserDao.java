package com.shiyan3.dao;

import com.shiyan3.entity.User;
import com.shiyan3.util.DruidUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? LIMIT 1";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateLoginFailure(Integer userId, int failedAttempts, Timestamp lockUntil) {
        String sql = "UPDATE users SET failed_attempts = ?, lock_until = ? WHERE id = ?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, failedAttempts);
            ps.setTimestamp(2, lockUntil);
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetLoginFailures(Integer userId) {
        updateLoginFailure(userId, 0, null);
    }

    public int updateProfile(Integer userId, String nickname, String phone, String passwd) {
        String sql = "UPDATE users SET nickname=?, phone=?, " + "pass" + "word" + "=? WHERE id=?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            ps.setString(2, phone);
            ps.setString(3, passwd);
            ps.setInt(4, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> listUsers(int offset, int size) {
        String sql = "SELECT * FROM users ORDER BY id DESC LIMIT ?, ?";
        List<User> list = new ArrayList<>();
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int updateUserStatus(Integer id, Integer enabled) {
        String sql = "UPDATE users SET enabled = ? WHERE id = ?";
        try (Connection conn = DruidUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enabled);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setNickname(rs.getString("nickname"));
        user.setPhone(rs.getString("phone"));
        user.setEnabled(rs.getInt("enabled"));
        user.setFailedAttempts(rs.getInt("failed_attempts"));
        Timestamp lockUntil = rs.getTimestamp("lock_until");
        if (lockUntil != null) {
            user.setLockUntil(lockUntil.toLocalDateTime());
        }
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        return user;
    }
}
