package com.example.booking.repository;

import com.example.booking.db.Booking;
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
}
