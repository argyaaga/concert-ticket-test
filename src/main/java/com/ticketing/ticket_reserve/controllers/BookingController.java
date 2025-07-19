package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.entities.Booking;
import com.ticketing.ticket_reserve.requests.booking.CreateBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.CreateBulkBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.UpdateBookingRequest;
import com.ticketing.ticket_reserve.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping(path = "api/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable Integer bookingId) {
        Booking result = bookingService.getBooking(bookingId);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Integer bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>("Booking with ID "+bookingId+" has been deleted", HttpStatus.OK);
    }

    @GetMapping
    public List<Booking> getBookings() {
        return bookingService.getBookings();
    }

    @GetMapping("/timeslot/{timeSlotId}")
    public List<Booking> getBookings(@PathVariable Integer timeSlotId) {
        return bookingService.getTimeSlotBookings(timeSlotId);
    }

    @PostMapping("/update")
    public ResponseEntity<Booking> updateBooking(@RequestBody UpdateBookingRequest bookingRequest) {
        Booking result = bookingService.updateBooking(bookingRequest);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody CreateBookingRequest bookingRequest) {
        Booking createdBooking = bookingService.createBooking(bookingRequest);
        if (createdBooking == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @PostMapping("/create_bulk")
    public ResponseEntity<List<Booking>> createBulkBooking(@RequestBody CreateBulkBookingRequest bookingRequest) {
        List<Booking> createdBookings = bookingService.createBulkBooking(bookingRequest);
        if (createdBookings == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(createdBookings, HttpStatus.CREATED);
    }
}
