package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.config.DatabaseConfig;
import com.example.dto.SignUpRequestDto;
import com.example.models.User;
import com.example.util.PasswordUtil;

public class AuthDao {

    public User authenticate(String email, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();

            String sql = "SELECT * FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");

                if (PasswordUtil.verifyPassword(password, storedPassword)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));

                    return user;
                }
            }
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null) {
                DatabaseConfig.getInstance().closeConnection(conn);
            }
        }
        return null;
    }

    public User createUser(SignUpRequestDto signUpRequestDto) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();

            String sql = "INSERT INTO users(name, email, password_hash, role) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, signUpRequestDto.getUsername());
            pstmt.setString(2, signUpRequestDto.getEmail());
            pstmt.setString(3, PasswordUtil.hashPassword(signUpRequestDto.getPassword()));
            pstmt.setString(4, signUpRequestDto.getRole());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                signUpRequestDto.setPassword(null); // Clear password before returning
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(signUpRequestDto.getUsername());
                user.setEmail(signUpRequestDto.getEmail());
                user.setRole(signUpRequestDto.getRole());
                return user;
            }
            return null;
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null) {
                DatabaseConfig.getInstance().closeConnection(conn);
            }
        }
    }
}