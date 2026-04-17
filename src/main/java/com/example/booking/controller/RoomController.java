package com.example.booking.controller;

import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.LoginResponse;
import com.example.booking.dto.RoomDto;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final AuthenticationJwtService authenticationJwtService;

    @GetMapping("/view")
    public ResponseEntity<String> getRooms() {
        return ResponseEntity.ok("No rooms present");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRoom(@RequestBody RoomDto roomDto) {
        roomService.addRoom(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Room added successfully");
    }


    @PostMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Room deleted successfully");
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
