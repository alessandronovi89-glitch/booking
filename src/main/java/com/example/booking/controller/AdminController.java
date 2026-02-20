package com.example.booking.controller;

import com.example.booking.dto.UserDto;
import com.example.booking.dto.UserViewDto;
import com.example.booking.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/info")
    public String infoProject() {
        return "This is a booking security project, welcome!";
    }

    @GetMapping("/user-list")
    public List<UserViewDto> users() {
        return userService.getUsers();
    }

    @PostMapping("/save-user")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.ok("User created successfully");
    }

}
