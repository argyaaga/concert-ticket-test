package com.ticketing.ticket_reserve.repositories;

import com.ticketing.ticket_reserve.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookingTimeSlotTimeSlotId(Integer bookingTimeSlotTimeSlotId);
}
