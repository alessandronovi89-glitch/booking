package com.example.booking.service;

import com.example.booking.db.Booking;
import com.example.booking.db.Room;
import com.example.booking.db.User;
import com.example.booking.dto.*;
import com.example.booking.model.BookingSummary;
import com.example.booking.model.enums.BookingStatus;
import com.example.booking.repository.BookRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DtoMapping dtoMapping;


    public void bookRoom(BookingRequest bookingRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        validateBookingRequest(bookingRequest);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setCheckIn(bookingRequest.getCheckInDate());
        booking.setTotalPrice(room.getPricePerNight().multiply(BigDecimal.valueOf(bookingRequest.getCheckOutDate().toEpochDay() - bookingRequest.getCheckInDate().toEpochDay())));
        booking.setCheckOut(bookingRequest.getCheckOutDate());
        booking.setCreatedAt(LocalDateTime.now());
        bookRepository.save(booking);
    }

    private void validateBookingRequest(BookingRequest bookingRequest) {
        if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date must be before check-out date");
        }
        if (bookingRequest.getCheckInDate().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date must be in the future");
        }
        if (bookingRequest.getCheckOutDate().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be in the future");
        }
        //facciamo finta per adesso che lo user è sempre abilitato
        if (!bookRepository.findOverlappingBookings(bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())
                .isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Room is not available for the selected dates");
        }
    }

    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getUser().getId().equals(userId)) {
            //TODO vedere e usare method level security..
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookRepository.save(booking);
    }

    public RangesBookedRoom showReservationsByRoom(Long roomId) {
        List<Booking> bookings = bookRepository.findAllBookedByRoomId(roomId);
        RangesBookedRoom rangesBookedRoom = new RangesBookedRoom();
        rangesBookedRoom.setRoomId(roomId);
        rangesBookedRoom.setRangeDates(bookings.stream().map(b -> new RangesBookedRoom.RangeDate(b.getCheckIn(), b.getCheckOut())).toList());
        return rangesBookedRoom;
    }

    //TODO fuso orario, come lo gestiamo? -> vedremo
    //TODO vedere problemi di concorrenza, ripassare..
    // (TODO fare tests)
    //fare magari event driven in questo progetto.. (leggere della teoria e capire cosa si può fare su questo progetto)
    //stesso dto.. todo
    public RoomBookingByStatusDto showMyReservations(Long userId) {
        return toRoomBookingByStatusDto(bookRepository.findAllActualBookingByUser(userId, LocalDate.now()));
    }

    public RoomBookingByStatusDto showMyOldReservations(Long userId) {
        return toRoomBookingByStatusDto(bookRepository.findOldBookingByUser(userId, LocalDate.now()));
    }

    private RoomBookingByStatusDto toRoomBookingByStatusDto(List<BookingSummary> bookings) {
        RoomBookingByStatusDto roomBookingByStatusDto = new RoomBookingByStatusDto();
        roomBookingByStatusDto.setBooked(bookings.stream().map(dtoMapping::roomBookingDtoFromSummaryBooking)
                .filter(RoomBookingDto::isBooked).toList());
        roomBookingByStatusDto.setCancelled(bookings.stream().map(dtoMapping::roomBookingDtoFromSummaryBooking)
                .filter(RoomBookingDto::isCancelled).toList());
        return roomBookingByStatusDto;
    }
}
