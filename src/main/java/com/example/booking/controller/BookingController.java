package com.example.booking.controller;

import com.example.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final UserService userService;

    @GetMapping("/view-rooms")
    public ResponseEntity<String> getRooms() {
        return ResponseEntity.ok("No rooms present");
    }

    @PostMapping("/add-room")
    public String addRoom() {
        return "Room added successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        //TODO : implement login logic (loginDto (username, password) -> authenticate -> generate token)
        //rimozione del filtro JwtTokenGenerator
        return ResponseEntity.ok("login successful");
    }

}
