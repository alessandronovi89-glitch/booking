package com.example.booking.service;

import com.example.booking.db.User;
import com.example.booking.dto.DtoMapping;
import com.example.booking.dto.UserDto;
import com.example.booking.dto.UserViewDto;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DtoMapping dtoMapping;
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public List<UserViewDto> getUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(dtoMapping::userViewDtoFromUser).toList();
    }

    @SneakyThrows
    public void saveUser(UserDto userDto){
        checkCompromisedPassword(userDto);
        User user = dtoMapping.userFromUserDto(userDto, passwordEncoder);
        userRepository.save(user);
    }

    //"todo https..un po' dappertutto..."
    private void checkCompromisedPassword(UserDto userDto) {
        if(compromisedPasswordChecker.check(userDto.getPassword()).isCompromised()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is compromised, choose another one");
        }
    }

}

