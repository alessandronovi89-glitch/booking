package com.example.booking.security;

import com.example.booking.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider
{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        if (passwordEncoder.matches(password, userService.getUser(username).getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    username, password, List.of());
        } else {
            throw new AuthenticationCredentialsNotFoundException("Error!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //??
        return UsernamePasswordAuthenticationToken
                .class
                .isAssignableFrom(authentication);
    }
}
