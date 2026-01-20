package com.example.dao;

import java.sql.Connection;
import com.example.models.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.config.DatabaseConfig;
import com.example.models.DriverLocation;

public class DriverDao {

    public void createDriver(Driver driver) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "INSERT INTO drivers (driver_id, vehicle_type, vehicle_number, status) values(?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, driver.getDriverId());
            pstmt.setString(2, driver.getVehicleType());
            pstmt.setString(3, driver.getVehicleNumber());
            pstmt.setString(4, driver.getStatus().toString());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DriverLocation getDriverLocationById(int driverId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM driver_locations WHERE driver_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, driverId);
            rs = stmt.executeQuery();
            DriverLocation driverLocation = new DriverLocation();
            if (rs.next()) {
                driverLocation.setDriverId(rs.getInt("driver_id"));
                driverLocation.setLatitude(rs.getDouble("latitude"));
                driverLocation.setLongitude(rs.getDouble("longitude"));
                driverLocation.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
            return driverLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDriverLocation(int driverId, double latitude, double longitude) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();

            String sql = "INSERT INTO driver_locations (driver_id, latitude, longitude, updated_at) " +
                    "VALUES (?, ?, ?, now()) " +
                    "ON CONFLICT (driver_id) DO UPDATE SET " +
                    "latitude = EXCLUDED.latitude, " +
                    "longitude = EXCLUDED.longitude, " +
                    "updated_at = now()";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, driverId);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
