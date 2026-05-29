package com.example.booking.dto;

import com.example.booking.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomBookingDto {
    private Long roomId;
    private String roomName;
    private String roomDescription;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalPrice;
    private BookingStatus status;

    public boolean isBooked() {
        return status == BookingStatus.BOOKED;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }
}
