package com.ticketing.ticket_reserve.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookingtimeslots")
public class BookingTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeSlotId;
//    private Integer concertId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert.concertId")
    private Concert concert;

    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer maxBookingCount;

    @OneToMany(mappedBy = "bookingtimeslot", fetch = FetchType.LAZY)
    private List<Booking> bookingList = new ArrayList<>();



}
