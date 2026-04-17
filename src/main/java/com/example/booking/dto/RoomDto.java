package com.example.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    //TODO > 0
    private BigDecimal pricePerNight;
}
