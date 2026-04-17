package com.example.booking.repository;

import com.example.booking.db.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Booking, Long> {
}
