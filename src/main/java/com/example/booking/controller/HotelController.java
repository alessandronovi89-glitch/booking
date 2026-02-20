package com.example.booking.controller;

import com.example.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/hotel")
public class HotelController {
    private final UserService userService;

    @PostMapping("/add-room")
    public String addRoom() {
        return "Room added successfully";
    }


}
