package com.example.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.dto.RideDetailsDto;
import com.example.dto.RideRequestDto;
import com.example.models.ApiResponse;
import com.example.models.RideRequest;
import com.example.models.Ride;
import com.example.service.RideService;

@Path("/rides")
public class RideResource {

        @POST
        @Path("/request")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response requestRide(
                        RideRequestDto rideRequestDto,
                        @Context HttpServletRequest req) {

                try {
                        HttpSession session = req.getSession(false);

                        if (session == null || session.getAttribute("userId") == null) {
                                return Response.status(Response.Status.UNAUTHORIZED)
                                                .entity(ApiResponse.error("Unauthorized", "Please login again"))
                                                .build();
                        }

                        int userId = (int) session.getAttribute("userId");

                        RideService rideService = new RideService();
                        RideRequest rideRequest = rideService.requestRide(userId, rideRequestDto);

                        return Response.ok(
                                        ApiResponse.success("Ride requested successfully.", rideRequest)).build();

                } catch (Exception e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(ApiResponse.error(
                                                        "An error occurred while requesting the ride.",
                                                        e.getMessage()))
                                        .build();
                }
        }

        @GET
        @Path("/{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getRideRequest(
                        @PathParam("id") int id,
                        @Context HttpServletRequest req) throws Exception {
                HttpSession session = req.getSession(false);

                if (session == null) {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                }

                // int userId = (int) session.getAttribute("userId");

                RideService rideService = new RideService();
                RideRequest ride = rideService.getRideRequestById(id);

                return Response.ok(
                                ApiResponse.success("Ride fetched", ride)).build();
        }

        @POST
        @Path("/accept")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response acceptRide(
                        RideRequestDto rideRequestDto,
                        @Context HttpServletRequest req) {

                try {
                        HttpSession session = req.getSession(false);

                        if (session == null || session.getAttribute("userId") == null) {
                                return Response.status(Response.Status.UNAUTHORIZED)
                                                .entity(ApiResponse.error("Unauthorized", "Please login again"))
                                                .build();
                        }

                        int driverId = (int) session.getAttribute("userId");

                        RideService rideService = new RideService();
                        RideRequest rideRequest = rideService.acceptRide(driverId, rideRequestDto);

                        if (rideRequest == null) {
                                return Response.status(Response.Status.BAD_REQUEST)
                                                .entity(ApiResponse.error("Ride not accepted", "Ride not found"))
                                                .build();
                        }
                        return Response.ok(
                                        ApiResponse.success("Ride accepted successfully.", rideRequest)).build();

                } catch (Exception e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(ApiResponse.error(
                                                        "An error occurred while accepting the ride.",
                                                        e.getMessage()))
                                        .build();
                }
        }

        @POST
        @Path("/start/{rideRequestId}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response startRide(
                        @PathParam("rideRequestId") String rideRequestId,
                        @Context HttpServletRequest req) {

                try {
                        HttpSession session = req.getSession(false);

                        if (session == null || session.getAttribute("userId") == null) {
                                return Response.status(Response.Status.UNAUTHORIZED)
                                                .entity(ApiResponse.error("Unauthorized", "Please login again"))
                                                .build();
                        }

                        int driverId = (int) session.getAttribute("userId");

                        RideService rideService = new RideService();
                        Ride ride = rideService.startRide(driverId, Integer.parseInt(rideRequestId));

                        if (ride == null) {
                                return Response.status(Response.Status.BAD_REQUEST)
                                                .entity(ApiResponse.error("Ride not started", "Ride not found"))
                                                .build();
                        }

                        return Response.ok(
                                        ApiResponse.success("Ride started successfully.", ride)).build();

                } catch (Exception e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(ApiResponse.error(
                                                        "An error occurred while starting the ride.",
                                                        e.getMessage()))
                                        .build();
                }
        }

        @POST
        @Path("/end/{rideRequestId}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response endRide(
                        @PathParam("rideRequestId") String rideRequestId,
                        @Context HttpServletRequest req) {

                try {
                        HttpSession session = req.getSession(false);

                        if (session == null || session.getAttribute("userId") == null) {
                                return Response.status(Response.Status.UNAUTHORIZED)
                                                .entity(ApiResponse.error("Unauthorized", "Please login again"))
                                                .build();
                        }

                        int driverId = (int) session.getAttribute("userId");

                        RideService rideService = new RideService();
                        Ride ride = rideService.endRide(driverId, Integer.parseInt(rideRequestId));

                        if (ride == null) {
                                return Response.status(Response.Status.BAD_REQUEST)
                                                .entity(ApiResponse.error("Ride not ended", "Ride not found"))
                                                .build();
                        }
                        return Response.ok(
                                        ApiResponse.success("Ride ended successfully.", ride)).build();

                } catch (Exception e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(ApiResponse.error(
                                                        "An error occurred while ending the ride.",
                                                        e.getMessage()))
                                        .build();
                }
        }

        @GET
        @Path("/active/{rideRequestId}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getActiveRideDetails(
                        @PathParam("rideRequestId") int rideRequestId,
                        @Context HttpServletRequest req) {

                try {
                        HttpSession session = req.getSession(false);
                        if (session == null || session.getAttribute("userId") == null) {
                                return Response.status(Response.Status.UNAUTHORIZED)
                                                .entity(ApiResponse.error("Unauthorized", "Please login again"))
                                                .build();
                        }

                        RideService rideService = new RideService();
                        RideDetailsDto rideDetails = rideService.getRideDetails(rideRequestId);

                        if (rideDetails == null) {
                                return Response.status(Response.Status.NOT_FOUND)
                                                .entity(ApiResponse.error("Not Found",
                                                                "Ride details not found or ride not yet accepted."))
                                                .build();
                        }

                        return Response.ok(ApiResponse.success("Ride details fetched", rideDetails)).build();

                } catch (Exception e) {
                        e.printStackTrace();
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(ApiResponse.error("Error", e.getMessage())).build();
                }
        }
}
