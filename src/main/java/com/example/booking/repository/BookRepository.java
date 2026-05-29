package com.example.booking.repository;

import com.example.booking.db.Booking;
import com.example.booking.model.BookingSummary;
import com.example.booking.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
            AND b.checkIn < :checkOut
            AND b.checkOut > :checkIn
            """)
    List<Booking> findOverlappingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    default List<Booking> findAllBookedByRoomId(Long roomId) {
        return findAllByRoomIdAndStatus(roomId, BookingStatus.BOOKED);
    }

    List<Booking> findAllByRoomIdAndStatus(Long roomId, BookingStatus bookingStatus);

    //TODO rivedi un po' spring data x favore..:
    //AND b.checkOut >= :now() -> NO!,
    //stai usando JPL, non SQL, quindi non puoi usare funzioni di database come now(), devi passare la data attuale come parametro
    //->stai lavorando a livello ORM
    //constructor expression (DTO classico)
    @Query("""
            SELECT new com.example.booking.model.BookingSummary(
                r.id, r.name, r.description,
                b.checkIn, b.checkOut, b.totalPrice, b.status)
            FROM Booking b 
            INNER JOIN b.room r
            WHERE b.user.id = :userId
            AND b.checkOut >= :today
            ORDER BY b.checkIn
            """)
    List<BookingSummary> findAllActualBookingByUser(
            @Param("userId") Long userId,
            @Param("today") LocalDate today
    );

    @Query("""
            SELECT new com.example.booking.model.BookingSummary(
                r.id, r.name, r.description,
                b.checkIn, b.checkOut, b.totalPrice, b.status)
            FROM Booking b 
            INNER JOIN b.room r
            WHERE b.user.id = :userId
            AND b.checkOut <:today
            ORDER BY b.checkIn
            """)
    List<BookingSummary> findOldBookingByUser(
            @Param("userId") Long userId,
            @Param("today") LocalDate today
    );


}
