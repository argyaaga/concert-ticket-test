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
    @JoinColumn(name = "bookingTimeSlot.timeSlotId")
    private BookingTimeSlot bookingTimeSlot;

    private LocalDateTime bookingTime;
    private Integer userId;

    public Booking(){}

    public Booking(BookingTimeSlot bookingTimeSlot, LocalDateTime bookingTime, Integer userId) {
        this.bookingTimeSlot = bookingTimeSlot;
        this.bookingTime = bookingTime;
        this.userId = userId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public BookingTimeSlot getBookingTimeSlot() {
        return bookingTimeSlot;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
