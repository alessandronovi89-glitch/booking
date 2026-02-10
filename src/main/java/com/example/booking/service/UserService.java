package com.example.booking.service;

import com.example.booking.db.User;
import com.example.booking.dto.DtoMapping;
import com.example.booking.dto.UserDto;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapping dtoMapping;

    public List<UserDto> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(dtoMapping::userDtoFromUser).toList();
    }

    public void saveUser(UserDto userDto){
        userRepository.save(dtoMapping.userFromUserDto(userDto));
    }

    public boolean checkUser(String username, String password){
        User user = userRepository.findByUsernameAndPassword(username, password);
        return user != null;
    }
}

