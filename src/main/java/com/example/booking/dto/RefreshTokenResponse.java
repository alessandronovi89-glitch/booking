package com.example.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RefreshTokenResponse {
    private String message;
    private String jwtToken;
    @JsonIgnore
    private String refreshToken;
}
