package com.example.booking.service;

import com.example.booking.db.User;
import com.example.booking.dto.DtoMapping;
import com.example.booking.dto.UserDto;
import com.example.booking.dto.UserViewDto;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapping dtoMapping;
    private final PasswordEncoder passwordEncoder;

    public List<UserViewDto> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(dtoMapping::userViewDtoFromUser).toList();
    }

    public void saveUser(UserDto userDto){
        User user = dtoMapping.userFromUserDto(userDto, passwordEncoder);
        userRepository.save(user);
    }

    public User getUser(String username){
        return userRepository.findByUsername(username);
    }
}

