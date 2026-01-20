package com.example.service;

import com.example.dao.LocationDao;
import com.example.dao.RideDao;
import com.example.dto.RideRequestDto;
import com.example.enums.RideRequestStatus;
import com.example.models.Location;
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
}
