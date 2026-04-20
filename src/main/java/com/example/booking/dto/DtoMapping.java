package com.example.booking.dto;

import com.example.booking.db.Room;
import com.example.booking.db.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface DtoMapping {

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    User userFromUserDto(UserDto userDto, @Context PasswordEncoder passwordEncoder);

    @Named("encodePassword")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(rawPassword);
    }

    UserViewDto userViewDtoFromUser(User user);

    Room roomDtoToRoom(RoomDto roomDto);

    RoomDto roomDtoFromRoom(Room room);

}
