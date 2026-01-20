package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.models.RideRequest;
import com.example.util.DistanceUtil;
import com.example.dao.DriverDao;
import com.example.dao.LocationDao;
import com.example.dao.RideDao;
import com.example.models.Driver;
import com.example.models.DriverLocation;
import com.example.models.Location;

public class DriverService {
    private static final double SEARCH_RADIUS_KM = 5.0;

    public void createDriver(Driver driver) {
        DriverDao driverDao = new DriverDao();
        driverDao.createDriver(driver);
    }

    public List<RideRequest> getNearByRideRequestsById(int driverId) throws Exception {
        DriverLocation driverLocation = getDriverLocation(driverId);
        RideDao rideDao = new RideDao();
        List<RideRequest> pendingRides = rideDao.getPendingRideRequests();

        List<RideRequest> nearbyRides = new ArrayList<>();
        for (RideRequest ride : pendingRides) {
            double distance = DistanceUtil.calculateDistance(
                    driverLocation.getLatitude(),
                    driverLocation.getLongitude(),
                    ride.getPickupLatitude(),
                    ride.getPickupLongitude());

            if (distance <= SEARCH_RADIUS_KM) {
                nearbyRides.add(ride);
            }
        }
        return nearbyRides;
    }

    public DriverLocation getDriverLocation(int driverId) {
        DriverDao driverDao = new DriverDao();

        return driverDao.getDriverLocationById(driverId);
    }

    public void setDriverLocation(int locationId, int driverId) throws Exception {
        DriverDao driverDao = new DriverDao();
        LocationDao locationDao = new LocationDao();
        Location location = locationDao.getLocationById(locationId);
        driverDao.setDriverLocation(driverId, location.getLatitude(), location.getLongitude());
    }
}
