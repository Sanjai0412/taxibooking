package com.example.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.models.ApiResponse;
import com.example.models.Location;
import com.example.service.LocationService;

@Path("/locations")
public class LocationResource {
    
    @GET
    @Path("/suggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchLocation(@QueryParam("locationName") String locationName){

        LocationService locationService = new LocationService();
        List<Location> locations = locationService.getLocationsByName(locationName);

        return Response.ok(ApiResponse.success("Locations retrieved successfully.", locations)).build();
    }
}
