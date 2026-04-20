package com.example.booking.service;

import com.example.booking.dto.DtoMapping;
import com.example.booking.dto.RoomDto;
import com.example.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final DtoMapping dtoMapping;

    public void addRoom(RoomDto roomDto) {
        roomRepository.saveAndFlush(dtoMapping.roomDtoToRoom(roomDto));
    }

    public void deleteRoom(Long idRoom) {
        roomRepository.deleteById(idRoom);
    }

    public List<RoomDto> viewRooms() {
        return roomRepository.findAll().stream()
                .map(dtoMapping::roomDtoFromRoom).toList();
    }
}
