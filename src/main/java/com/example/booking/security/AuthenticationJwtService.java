package com.example.booking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.example.booking.db.User;
import com.example.booking.dto.LoginResponse;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor //TODO NOTA
public class AuthenticationJwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-minutes}")
    private Integer expirationMinutes;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public LoginResponse loginAuthentication(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String jwtToken = generateToken(new CustomUserDetails(user));
        UUID refreshToken = sessionService.generateSession(user);
        return LoginResponse.builder()
                .jwtToken(jwtToken)
                .message("welcome to booking project, " + user.getUsername())
                .refreshToken(refreshToken.toString()).build();
    }

    private String generateToken(CustomUserDetails userDetails) {
        return JWT.create()
                .withIssuer("booking-app")
                .withSubject(userDetails.getUsername())
                .withArrayClaim("authorities", userDetails.getAuthorities().stream()
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
