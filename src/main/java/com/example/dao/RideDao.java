package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.config.DatabaseConfig;
import com.example.enums.RideRequestStatus;
import com.example.models.RideRequest;

public class RideDao {

    public RideRequest createRideRequest(RideRequest rideRequest) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "INSERT INTO ride_requests (user_id, pickup_location_id, pickup_label,  pickup_lat, pickup_lng, drop_location_id, drop_label, drop_lat, drop_lng, estimated_distance, estimated_fare, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, rideRequest.getUserId());
            pstmt.setInt(2, rideRequest.getPickupLocationId());
            pstmt.setString(3, rideRequest.getPickup());
            pstmt.setDouble(4, rideRequest.getPickupLatitude());
            pstmt.setDouble(5, rideRequest.getPickupLongitude());
            pstmt.setInt(6, rideRequest.getDropLocationId());
            pstmt.setString(7, rideRequest.getDrop());
            pstmt.setDouble(8, rideRequest.getDropLatitude());
            pstmt.setDouble(9, rideRequest.getDropLongitude());
            pstmt.setDouble(10, rideRequest.getEstimatedDistance());
            pstmt.setDouble(11, rideRequest.getEstimatedFare());
            pstmt.setString(12, rideRequest.getStatus());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int generatedId = rs.getInt("id");
                rideRequest.setId(generatedId);
                return rideRequest;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    DatabaseConfig.getInstance().closeConnection(conn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public RideRequest getRideRequestById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM ride_requests WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            RideRequest rideRequest = null;

            if (rs.next()) {
                rideRequest = new RideRequest();
                rideRequest.setId(rs.getInt("id"));
                rideRequest.setUserId(rs.getInt("user_id"));
                rideRequest.setPickupLocationId(rs.getInt("pickup_location_id"));
                rideRequest.setPickup(rs.getString("pickup_label"));
                rideRequest.setPickupLatitude(rs.getDouble("pickup_lat"));
                rideRequest.setPickupLongitude(rs.getDouble("pickup_lng"));
                rideRequest.setDropLocationId(rs.getInt("drop_location_id"));
                rideRequest.setDrop(rs.getString("drop_label"));
                rideRequest.setDropLatitude(rs.getDouble("drop_lat"));
                rideRequest.setDropLongitude(rs.getDouble("drop_lng"));
                rideRequest.setEstimatedDistance(rs.getDouble("estimated_distance"));
                rideRequest.setEstimatedFare(rs.getDouble("estimated_fare"));
                rideRequest.setStatus(rs.getString("status"));
                rideRequest.setCreatedAt(rs.getTimestamp("created_at"));
            }
            return rideRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    DatabaseConfig.getInstance().closeConnection(conn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<RideRequest> getPendingRideRequests() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM ride_requests WHERE status = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, RideRequestStatus.PENDING.toString());

            rs = pstmt.executeQuery();

            List<RideRequest> rideRequests = new ArrayList<>();
            while (rs.next()) {
                RideRequest rideRequest = new RideRequest();
                rideRequest.setId(rs.getInt("id"));
                rideRequest.setUserId(rs.getInt("user_id"));
                rideRequest.setPickupLocationId(rs.getInt("pickup_location_id"));
                rideRequest.setPickup(rs.getString("pickup_label"));
                rideRequest.setPickupLatitude(rs.getDouble("pickup_lat"));
                rideRequest.setPickupLongitude(rs.getDouble("pickup_lng"));
                rideRequest.setDropLocationId(rs.getInt("drop_location_id"));
                rideRequest.setDrop(rs.getString("drop_label"));
                rideRequest.setDropLatitude(rs.getDouble("drop_lat"));
                rideRequest.setDropLongitude(rs.getDouble("drop_lng"));
                rideRequest.setEstimatedDistance(rs.getDouble("estimated_distance"));
                rideRequest.setEstimatedFare(rs.getDouble("estimated_fare"));
                rideRequest.setStatus(rs.getString("status"));
                rideRequest.setCreatedAt(rs.getTimestamp("created_at"));

                rideRequests.add(rideRequest);
            }
            return rideRequests;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
