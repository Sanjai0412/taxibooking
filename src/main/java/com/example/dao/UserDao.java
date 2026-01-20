package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.models.User;
import com.example.enums.UserStatus;
import com.example.config.DatabaseConfig;

public class UserDao {

    public User getUserById(int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void changeUserStatus(UserStatus status, int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "UPDATE users SET status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, status.toString());
            ps.setInt(2, userId);

            ps.executeUpdate();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
