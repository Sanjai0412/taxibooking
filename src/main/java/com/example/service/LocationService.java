package com.example.service;

import java.util.List;

import com.example.dao.LocationDao;
import com.example.models.Location;

public class LocationService {
    
    public List<Location> getLocationsByName(String locationName){
        LocationDao locationDao = new LocationDao();
        try{
            List<Location> locations = locationDao.getLocationsByName(locationName);
            return locations;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
