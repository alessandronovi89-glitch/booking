package com.example.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class InfoController {

    @GetMapping("/info")
    public String infoProject() {
        return "This is a booking security project, welcome!";
    }

    @GetMapping("/username")
    public String infoUsername(Authentication authentication) {
        return "<h2> Welcome to booking project, " + authentication.getName() + "</h2>";
    }
    /*
    @GetMapping("/username")
    public String infoUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "<h2> Welcome to booking project, " + authentication.getName() + "</h2>";
    }*/


}
