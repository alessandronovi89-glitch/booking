package com.example.booking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.example.booking.configuration.TokenProperties;
import com.example.booking.service.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@AllArgsConstructor
public class JwtService {
    private final TokenProperties tokenProperties;

    public String generateToken(CustomUserDetails userDetails) {
        return JWT.create()
                .withIssuer("booking-app")
                .withSubject(userDetails.getUsername())
                .withArrayClaim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList().toArray(new String[0]))
                .withExpiresAt(Instant.now().plus(tokenProperties.getJwtExpirationMinutes(), MINUTES))
                .sign(getAlgorithm());
    }

    public Map<String, Claim> validateToken(String jwtToken) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).withIssuer("booking-app").build();
        return verifier.verify(jwtToken).getClaims();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(tokenProperties.getJwtSecretKey());
    }
}
