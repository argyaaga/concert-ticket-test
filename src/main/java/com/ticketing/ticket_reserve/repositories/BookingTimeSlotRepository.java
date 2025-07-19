package com.ticketing.ticket_reserve.repositories;

import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingTimeSlotRepository extends JpaRepository<BookingTimeSlot, Integer> {
    List<BookingTimeSlot> findByConcertConcertId(Integer concertConcertId);
}
