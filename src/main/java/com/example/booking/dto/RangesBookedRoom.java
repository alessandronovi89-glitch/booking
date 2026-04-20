package com.example.booking.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RangesBookedRoom {
    private long roomId;
    private List<RangeDate> rangeDates;

    @Data
    @AllArgsConstructor
    public static class RangeDate {
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
    }
}



