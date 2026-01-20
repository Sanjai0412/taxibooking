package com.example.resource;

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

import com.example.dto.LoginRequestDto;
import com.example.dto.SignUpRequestDto;
import com.example.dto.UserDto;
import com.example.enums.UserStatus;
import com.example.mapper.UserMapper;
import com.example.models.ApiResponse;
import com.example.models.User;
import com.example.service.AuthService;
import com.example.service.UserService;

@Path("/auth")
public class AuthResource {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDto loginRequest, @Context HttpServletRequest req) {
        try {

            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null
                    || loginRequest.getEmail().trim().isEmpty() || loginRequest.getPassword().trim().isEmpty()) {

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Email and password are required!"))
                        .build();
            }

            AuthService authService = new AuthService();
            UserDto userDto = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (userDto == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(ApiResponse.error("Invalid email or password."))
                        .build();
            }

            UserService userService = new UserService();
            userService.changeUserStatus(UserStatus.ACTIVE, userDto.getId());

            req.getSession().setAttribute("userId", userDto.getId());

            return Response.ok(ApiResponse.success("Login successful.", userDto)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred during login."))
                    .build();
        }
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(SignUpRequestDto signUpRequestDto, @Context HttpServletRequest req) {

        try {
            if (signUpRequestDto.getUsername() == null || signUpRequestDto.getEmail() == null
                    || signUpRequestDto.getPassword() == null
                    || signUpRequestDto.getUsername().trim().isEmpty() || signUpRequestDto.getEmail().trim().isEmpty()
                    || signUpRequestDto.getPassword().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Username, email and password are required!"))
                        .build();
            }
            AuthService authService = new AuthService();
            User user = authService.registerUser(signUpRequestDto);

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Signup failed. Email may already be in use."))
                        .build();
            }
            req.getSession().setAttribute("userId", user.getId());

            return Response.ok(ApiResponse.success("Signup successful.", UserMapper.toDto(user))).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("An error occurred during signup."))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest req) {
        UserService userService = new UserService();
        HttpSession session = req.getSession(false);
        if (session == null) {
            return Response.status(401).build();
        }
        try {
            userService.changeUserStatus(UserStatus.INACTIVE, (Integer) session.getAttribute("userId"));
            session.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        return Response.ok(ApiResponse.success("Logged out successfully", null)).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@Context HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            return Response.status(401).build();
        }
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error("User ID not found in session")).build();
        }
        UserService userService = new UserService();
        try {
            User user = userService.getUserById(Integer.parseInt(userId.toString()));
            // Check if user exists (could be null if deleted but session remains)
            if (user == null) {
                session.invalidate();
                return Response.status(Response.Status.UNAUTHORIZED).entity(ApiResponse.error("User not found"))
                        .build();
            }
            return Response.ok(ApiResponse.success("Session checked", UserMapper.toDto(user))).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error("Error checking session")).build();
        }
    }
}