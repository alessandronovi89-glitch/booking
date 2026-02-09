package com.example.booking.controller;

import com.example.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookingController {
    private final UserService userService;

    @GetMapping("/user")
    public String hello() {
        return "Hello, user!";
    }

    @GetMapping("/info")
    public String infoProject() {
        return "This is a booking security project, welcome!";
    }

    @GetMapping("/user-list")
    public List<String> users() {
        return userService.getUsers();
    }

}
