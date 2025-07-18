package com.ticketing.ticket_reserve.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingtimeslot.timeSlotId")
    private BookingTimeSlot bookingTimeSlot;

    private LocalDateTime bookingTime;
    private Integer userID;
}
