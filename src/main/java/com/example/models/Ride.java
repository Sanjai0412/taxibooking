package com.example.models;

import java.sql.Timestamp;

import com.example.enums.RideStatus;

public class Ride {
    private int id;
    private int driverId;
    private int rideRequestId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double finalFare;
    private double finalDistance;
    private RideStatus status;

    public Ride() {
    }

    public Ride(int id, int driverId, int rideRequestId, Timestamp startTime, Timestamp endTime, double finalFare,
            double finalDistance, RideStatus status) {
        this.id = id;
        this.driverId = driverId;
        this.rideRequestId = rideRequestId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalFare = finalFare;
        this.finalDistance = finalDistance;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getRideRequestId() {
        return rideRequestId;
    }

    public void setRideRequestId(int rideRequestId) {
        this.rideRequestId = rideRequestId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public double getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(double finalFare) {
        this.finalFare = finalFare;
    }

    public double getFinalDistance() {
        return finalDistance;
    }

    public void setFinalDistance(double finalDistance) {
        this.finalDistance = finalDistance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

}
