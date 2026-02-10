package com.example.booking.dto;

import com.example.booking.db.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DtoMapping {

    UserDto userDtoFromUser(User user);

    User userFromUserDto(UserDto userDto);

}
