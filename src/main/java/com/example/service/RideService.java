package com.example.service;

import java.sql.Timestamp;

import com.example.dao.DriverDao;
import com.example.dao.LocationDao;
import com.example.dao.RideDao;
import com.example.dto.RideDetailsDto;
import com.example.dto.RideRequestDto;
import com.example.enums.DriverStatus;
import com.example.enums.RideRequestStatus;
import com.example.enums.RideStatus;
import com.example.models.Location;
import com.example.models.Ride;
import com.example.models.RideRequest;
import com.example.util.DistanceUtil;

public class RideService {

    public RideRequest requestRide(int userId, RideRequestDto rideRequestDto) throws Exception {
        // Logic to handle ride request
        LocationDao locationDao = new LocationDao();
        Location pickupLocation = locationDao.getLocationById(rideRequestDto.getPickupLocationId());
        Location dropLocation = locationDao.getLocationById(rideRequestDto.getDropLocationId());

        RideDao rideDao = new RideDao();
        RideRequest rideRequest = new RideRequest();
        rideRequest.setUserId(userId);
        rideRequest.setPickupLocationId(rideRequestDto.getPickupLocationId());
        rideRequest.setPickup(pickupLocation.getName());
        rideRequest.setPickupLatitude(pickupLocation.getLatitude());
        rideRequest.setPickupLongitude(pickupLocation.getLongitude());
        rideRequest.setDropLocationId(rideRequestDto.getDropLocationId());
        rideRequest.setDrop(dropLocation.getName());
        rideRequest.setDropLatitude(dropLocation.getLatitude());
        rideRequest.setDropLongitude(dropLocation.getLongitude());
        double distance = calculateDistanceByLocationIds(rideRequestDto.getPickupLocationId(),
                rideRequestDto.getDropLocationId());
        rideRequest.setEstimatedDistance(distance);
        rideRequest.setEstimatedFare(calculateFare(distance));
        rideRequest.setStatus(RideRequestStatus.PENDING.toString());

        return rideDao.createRideRequest(rideRequest);
    }

    public double calculateDistanceByLocationIds(int pickupLocationId, int dropLocationId) throws Exception {
        // Get pickup and drop locations lat and long
        LocationDao locationDao = new LocationDao();
        Location pickupLocation = locationDao.getLocationById(pickupLocationId);
        Location dropLocation = locationDao.getLocationById(dropLocationId);

        return DistanceUtil.calculateDistance(
                pickupLocation.getLatitude(), pickupLocation.getLongitude(),
                dropLocation.getLatitude(), dropLocation.getLongitude());
    }

    public double calculateFare(double distance) throws Exception {
        // Logic to calculate fare
        return distance * 10;
    }

    public RideRequest getRideRequestById(int id) throws Exception {
        // Logic to get ride request by id
        RideDao rideDao = new RideDao();
        return rideDao.getRideRequestById(id);
    }

    public RideRequest acceptRide(int driverId, RideRequestDto rideRequestDto) {
        RideDao rideDao = new RideDao();
        RideRequest rideRequest = rideDao.getRideRequestById(rideRequestDto.getId());
        rideRequest.setStatus(RideRequestStatus.ACCEPTED.toString());

        // Create Ride record with ACCEPTED status
        Ride ride = new Ride();
        ride.setDriverId(driverId);
        ride.setRideRequestId(rideRequest.getId());
        ride.setStatus(RideStatus.ACCEPTED);
        // Note: startTime is not set yet, it will be set when ride starts
        rideDao.createRide(ride);

        return rideDao.updateRideRequest(rideRequest);
    }

    public Ride startRide(int driverId, int rideRequestId) {
        RideDao rideDao = new RideDao();
        Ride ride = getRideByRequestId(rideRequestId);

        if (ride == null) {
            // Fallback for legacy flow or error
            ride = new Ride();
            ride.setDriverId(driverId);
            ride.setRideRequestId(rideRequestId);
            ride.setStatus(RideStatus.STARTED);
            ride.setStartTime(new Timestamp(System.currentTimeMillis()));
            ride = rideDao.createRide(ride);
        } else {
            ride.setStatus(RideStatus.STARTED);
            ride.setStartTime(new Timestamp(System.currentTimeMillis()));
            rideDao.updateRideStatus(ride);
        }

        DriverDao driverDao = new DriverDao();
        driverDao.setDriverStatus(driverId, DriverStatus.ON_RIDE);

        return ride;
    }

    public Ride endRide(int driverId, int rideRequestId) throws Exception {
        RideDao rideDao = new RideDao();
        RideRequest rideRequest = getRideRequestById(rideRequestId);
        Ride ride = getRideByRequestId(rideRequestId);
        ride.setFinalDistance(rideRequest.getEstimatedDistance());
        ride.setFinalFare(rideRequest.getEstimatedFare());
        ride.setEndTime(new Timestamp(System.currentTimeMillis()));
        ride.setStatus(RideStatus.ENDED);

        DriverDao driverDao = new DriverDao();
        driverDao.setDriverStatus(driverId, DriverStatus.ONLINE);
        return rideDao.endRide(ride);
    }

    public Ride getRideByRequestId(int rideRequestId) {
        RideDao rideDao = new RideDao();
        return rideDao.getRideByRequestId(rideRequestId);
    }

    public RideDetailsDto getRideDetails(int rideRequestId) throws Exception {
        Ride ride = getRideByRequestId(rideRequestId);
        if (ride == null) {
            return null;
        }

        RideRequest rideRequest = getRideRequestById(rideRequestId);
        DriverDao driverDao = new DriverDao();
        com.example.models.Driver driver = driverDao.getDriverById(ride.getDriverId());
        com.example.dao.UserDao userDao = new com.example.dao.UserDao();
        com.example.models.User driverUser = userDao.getUserById(ride.getDriverId());

        RideDetailsDto dto = new com.example.dto.RideDetailsDto();
        dto.setRideId(ride.getId());
        dto.setStatus(ride.getStatus());
        dto.setDriverName(driverUser.getUsername());
        dto.setVehicleNumber(driver.getVehicleNumber());
        dto.setVehicleType(driver.getVehicleType());
        dto.setPickup(rideRequest.getPickup());
        dto.setDrop(rideRequest.getDrop());
        dto.setEstimatedFare(rideRequest.getEstimatedFare());
        dto.setEstimatedDistance(rideRequest.getEstimatedDistance());

        return dto;
    }
}