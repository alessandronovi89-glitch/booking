package com.example.booking.controller;

import com.example.booking.dto.RoomDto;
import com.example.booking.security.AuthenticationJwtService;
import com.example.booking.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final AuthenticationJwtService authenticationJwtService;

    @GetMapping("/view")
    public List<RoomDto> getRooms() {
        return roomService.viewRooms();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRoom(@RequestBody RoomDto roomDto) {
        roomService.addRoom(roomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Room added successfully");
    }


    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Room deleted successfully");
    }


}
