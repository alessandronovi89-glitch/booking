package com.example.booking.service;

import com.example.booking.db.Booking;
import com.example.booking.db.Room;
import com.example.booking.db.User;
import com.example.booking.dto.BookingRequest;
import com.example.booking.model.enums.BookingStatus;
import com.example.booking.repository.BookRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    public void bookRoom(BookingRequest bookingRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));

        validateBookingRequest(bookingRequest, user, room);
        //TODO -> validazioni:
        //user abilitato, valido?..
        //room disponibile?..
        //date check-in, check-out validi?..
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(BookingStatus.BOOKED);
        booking.setCheckIn(bookingRequest.getCheckInDate());
        booking.setCheckOut(bookingRequest.getCheckOutDate());
        booking.setCreatedAt(LocalDateTime.now());
        bookRepository.save(booking);
    }

    private void validateBookingRequest(BookingRequest bookingRequest, User user, Room room) {
        if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())) {
            throw new RuntimeException("Check-in date must be before check-out date");
        }
        if (bookingRequest.getCheckInDate().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new RuntimeException("Check-in date must be in the future");
        }
        if (bookingRequest.getCheckOutDate().isBefore(LocalDateTime.now().toLocalDate())) {
            throw new RuntimeException("Check-out date must be in the future");
        }
        //TODO campo enable -> possibilmente lo facciamo gia nella query, by username and enabled
            /*if(!user.isEnabled()){
                throw new RuntimeException("User is not enabled to book a room");
            }*/
        //è gia occupata la stanza?
        //find by id.. e controllo delle date.. da vedere come fare..
        //bookRepository.

        //TODO -> validazione disponibilità stanza per le date richieste
    }

    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookRepository.save(booking);
    }

}
