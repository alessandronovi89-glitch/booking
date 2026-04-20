package com.example.booking.controller;

import com.example.booking.dto.BookingRequest;
import com.example.booking.dto.RangesBookedRoom;
import com.example.booking.service.BookService;
import com.example.booking.service.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class BookController {
    private final BookService bookService;

    //hm questo va bene per fare i calcoli..
    //ma in realtà servirebbe lista di prenotazione dell'utente
    // -> modificare.. show reservation di tutte le stanza ma di uno specifico utente..
    //TODO da fare..
    @GetMapping
    public RangesBookedRoom showReservations(@RequestParam Long roomId) {
        return bookService.showReservations(roomId);
    }

    @PostMapping("/room")
    public ResponseEntity<String> bookRoom(@RequestBody BookingRequest bookingRequest,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        //TODO differenza tra CustomUserDetails e non Authentication authentication?
        bookService.bookRoom(bookingRequest, user.getId());
        return ResponseEntity.ok("Room booked successfully");
    }

    //Patch: aggiorni solo qualche cambio, put sostituzione completa della risorsa..
    @PatchMapping("/cancel/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId, @AuthenticationPrincipal CustomUserDetails user) {
        bookService.cancelBooking(bookingId, user.getId());
        return ResponseEntity.ok("Room cancelled successfully");
    }

}
