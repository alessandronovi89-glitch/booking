package com.example.booking.service;

import com.example.booking.db.User;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<String> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getFullName).toList();
    }
}

