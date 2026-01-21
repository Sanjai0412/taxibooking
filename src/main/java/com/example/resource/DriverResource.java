package com.example.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.models.ApiResponse;
import com.example.models.Driver;
import com.example.dto.DriverDto;
import com.example.dto.DriverLocationDto;
import com.example.enums.DriverStatus;
import com.example.models.RideRequest;
import com.example.service.DriverService;

@Path("/drivers")
public class DriverResource {

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDriver(DriverDto driverDto, @Context HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Unauthorized", "Please login again"))
                    .build();
        }
        int driverId = (int) session.getAttribute("userId");
        System.out.println(driverId);
        Driver driver = new Driver();
        driver.setDriverId(driverId);
        driver.setVehicleType(driverDto.getVehicleType());
        driver.setVehicleNumber(driverDto.getVehicleNumber());
        driver.setStatus(DriverStatus.ONLINE);
        driver.setCreatedAt(new java.util.Date().toString());

        try {
            DriverService driverService = new DriverService();
            driverService.createDriver(driver);

            return Response.ok(
                    ApiResponse.success("Driver created successfully", driver)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error(
                            "An error occurred while creating the driver.",
                            e.getMessage()))
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/me/ride-requests")
    public Response getRideRequests(@Context HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Unauthorized", "Please login again"))
                    .build();
        }
        int driverId = (int) session.getAttribute("userId");

        try {
            DriverService driverService = new DriverService();
            List<RideRequest> rideRequests = driverService.getNearByRideRequestsById(driverId);

            return Response.ok(
                    ApiResponse.success("Ride requests fetched successfully", rideRequests)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error(
                            "An error occurred while fetching the ride requests.",
                            e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/location")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setDriverLocation(DriverLocationDto driverLocation, @Context HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Unauthorized", "Please login again"))
                    .build();
        }
        int driverId = (int) session.getAttribute("userId");

        try {
            DriverService driverService = new DriverService();
            driverService.setDriverLocation(driverLocation.getLocationId(), driverId);

            return Response.ok(
                    ApiResponse.success("Driver location set successfully", driverLocation)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error(
                            "An error occurred while setting the driver location.",
                            e.getMessage()))
                    .build();
        }
    }
}
