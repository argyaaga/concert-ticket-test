package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.CreateBookingTimeSlotRequest;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.UpdateBookingTimeSlotRequest;
import com.ticketing.ticket_reserve.services.BookingTimeSlotService;
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

import java.util.List;

@RestController
@RequestMapping(path = "api/timeslot")
public class BookingTimeSlotController {
    private final BookingTimeSlotService timeSlotService;

    @Autowired
    public BookingTimeSlotController(BookingTimeSlotService timeSlotService){
        this.timeSlotService = timeSlotService;
    }

    @GetMapping("/{timeSlotId}")
    public ResponseEntity<BookingTimeSlot> getTimeSlot(@PathVariable Integer timeSlotId) {
        BookingTimeSlot result = timeSlotService.getTimeSlot(timeSlotId);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity<String> deleteTimeSlot(@PathVariable Integer timeSlotId) {
        timeSlotService.deleteTimeSlot(timeSlotId);
        return new ResponseEntity<>("Time Slot with ID "+timeSlotId+" has been deleted", HttpStatus.OK);
    }

    @GetMapping
    public List<BookingTimeSlot> getTimeSlots() {
        return timeSlotService.getTimeSlots();
    }

    @GetMapping("/concert/{concertId}")
    public List<BookingTimeSlot> getConcertTimeSlots(@PathVariable Integer concertId) {
        return timeSlotService.getConcertTimeSlots(concertId);
    }

    @GetMapping("/upcoming")
    public List<BookingTimeSlot> getUpcomingSlots() {
        return timeSlotService.getUpcomingSlots();
    }

    @GetMapping("/available")
    public List<BookingTimeSlot> getAvailableSlots() {
        return timeSlotService.getAvailableSlots();
    }

    @PostMapping("/update")
    public ResponseEntity<BookingTimeSlot> updateTimeSlot(@RequestBody UpdateBookingTimeSlotRequest bookingTimeSlotRequest) {
        BookingTimeSlot result = timeSlotService.updateTimeSlot(bookingTimeSlotRequest);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BookingTimeSlot> createTimeSlot(@RequestBody CreateBookingTimeSlotRequest bookingTimeSlotRequest) {
        BookingTimeSlot createdTimeSlot = timeSlotService.createTimeSlot(bookingTimeSlotRequest);
        if (createdTimeSlot == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdTimeSlot, HttpStatus.CREATED);
    }
}
