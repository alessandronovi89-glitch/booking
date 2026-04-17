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
@RequestMapping("/user")
public class AdminController {
    private final UserService userService;

    @GetMapping("/list")
    public List<UserViewDto> users() {
        return userService.getUsers();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

}
