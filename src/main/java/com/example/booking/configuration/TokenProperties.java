package com.example.booking.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TokenProperties {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.expiration-minutes}")
    private Integer jwtExpirationMinutes;
    @Value("${refresh-token.expiration-days}")
    private Integer refreshTokenExpirationDays;

}
