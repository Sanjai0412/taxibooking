package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.example.config.DatabaseConfig;
import com.example.models.Location;

public class LocationDao {

    public List<Location> getLocationsByName(String locationName) throws Exception {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM locations WHERE name ILIKE ? ORDER BY name LIMIT 10";
            ps = conn.prepareStatement(sql);
            ps.setString(1, locationName + "%");

            rs = ps.executeQuery();

            List<Location> locations = new ArrayList<>();

            while (rs.next()) {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setName(rs.getString("name"));
                location.setLatitude(rs.getDouble("latitude"));
                location.setLongitude(rs.getDouble("longitude"));

                locations.add(location);
            }
            return locations;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Location getLocationById(int id) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            String sql = "SELECT * FROM locations WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            Location location = null;

            if (rs.next()) {
                location = new Location();
                location.setId(rs.getInt("id"));
                location.setName(rs.getString("name"));
                location.setLatitude(rs.getDouble("latitude"));
                location.setLongitude(rs.getDouble("longitude"));
            }
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                DatabaseConfig.getInstance().closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
