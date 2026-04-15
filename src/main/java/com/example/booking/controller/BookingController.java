package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.LoginResponse;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final UserService userService;
    private final AuthenticationJwtService authenticationJwtService;

    @GetMapping("/view-rooms")
    public ResponseEntity<String> getRooms() {
        return ResponseEntity.ok("No rooms present");
    }

    @PostMapping("/add-room")
    public String addRoom() {
        return "Room added successfully";
    }

    @PostMapping("/delete-room")
    public String deleteRoom() {
        return "Room added successfully";
    }

    @PostMapping("/book-room")
    public String bookRoom() {
        return "Room booked successfully";
    }

    @PostMapping("/delete-booking")
    public String deleteBooking() {
        return "Room booked deleted successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                authenticationJwtService.loginAuthentication(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
    }

}
