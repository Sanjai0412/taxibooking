package com.example.models;

import com.example.enums.DriverStatus;

public class Driver {
    private int driverId;
    private String vehicleType;
    private String vehicleNumber;
    private DriverStatus status;
    private String createdAt;

    public Driver() {
    }

    public Driver(int driverId, String vehicleType, String vehicleNumber, DriverStatus status, String createdAt) {
        this.driverId = driverId;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
