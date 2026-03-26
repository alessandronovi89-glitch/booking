package com.example.booking.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secretKey;


    public String generateToken(org.springframework.security.core.Authentication authentication) {
        return JWT.create()
                .withIssuer("booking-app")
                .withSubject(authentication.getName())
                .withArrayClaim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList().toArray(new String[0]))
                .withExpiresAt(LocalDateTime.now().plus(30, ChronoUnit.MINUTES).toInstant(ZoneOffset.UTC))
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
