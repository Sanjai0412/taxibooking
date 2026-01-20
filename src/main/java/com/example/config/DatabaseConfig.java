package com.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {

    private static DatabaseConfig instance;
    private Dotenv dotenv;

    private DatabaseConfig() {
        try {
            Class.forName("org.postgresql.Driver");

            dotenv = Dotenv.configure().ignoreIfMissing().load();
            String dbUrl = dotenv.get("DATABASE_URL");
            String dbUser = dotenv.get("DB_USERNAME");

            if (dbUrl == null || dbUser == null) {
                System.err.println("CRITICAL ERROR: Environment variables missing from .env!");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Postgres Driver not found: " + e.getMessage());
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        String url = dotenv.get("DATABASE_URL");
        String user = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            System.err.println("CRITICAL: Missing env vars in getConnection. url=" + url + ", user=" + user);
            throw new SQLException("Cannot connect: Missing environment variables.");
        }

        return DriverManager.getConnection(url, user, password);
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Failed to close DB connection " + e.getMessage());
            }
        }
    }
}
