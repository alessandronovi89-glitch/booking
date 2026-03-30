package com.example.booking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.example.booking.dto.LoginResponse;
import com.example.booking.service.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor //TODO NOTA
public class AuthenticationJwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-minutes}")
    private Integer expirationMinutes;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse loginAuthentication(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String jwtToken = generateToken(new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities()));
        return LoginResponse.builder()
                .jwtToken(jwtToken)
                .message("welcome to booking project, " + userDetails.getUsername()).build();
    }

    private String generateToken(org.springframework.security.core.Authentication authentication) {
        return JWT.create()
                .withIssuer("booking-app")
                .withSubject(authentication.getName())
                .withArrayClaim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList().toArray(new String[0]))
                .withExpiresAt(Instant.now().plus(expirationMinutes, MINUTES))
                .sign(getAlgorithm());
    }

    public Map<String, Claim> validateToken(String jwtToken) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).withIssuer("booking-app").build();
        return verifier.verify(jwtToken).getClaims();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }
}
