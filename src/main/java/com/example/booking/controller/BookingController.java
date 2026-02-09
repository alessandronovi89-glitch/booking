package com.example.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    @GetMapping("/user")
    public String hello() {
        return "Hello, user!";
    }

    @GetMapping("/info")
    public String infoProject() {
        return "This is a booking security project, welcome!";
    }

}
