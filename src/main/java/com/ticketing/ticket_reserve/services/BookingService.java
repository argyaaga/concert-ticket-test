package com.ticketing.ticket_reserve.services;

import com.ticketing.ticket_reserve.entities.Booking;
import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import com.ticketing.ticket_reserve.repositories.BookingRepository;
import com.ticketing.ticket_reserve.repositories.BookingTimeSlotRepository;
import com.ticketing.ticket_reserve.requests.booking.CreateBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.CreateBulkBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.UpdateBookingRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingTimeSlotRepository bookingTimeSlotRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingTimeSlotRepository bookingTimeSlotRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingTimeSlotRepository = bookingTimeSlotRepository;
    }

    public List<Booking> getBookings() { return bookingRepository.findAll();}

    public Booking getBooking(Integer bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElse(null);
    }

    public List<Booking> getTimeSlotBookings(Integer bookingTimeSlotTimeSlotId) {
        return bookingRepository.findByBookingTimeSlotTimeSlotId(bookingTimeSlotTimeSlotId);
    }

    @Transactional
    public Booking createBooking(CreateBookingRequest bookingRequest) {
        Optional<BookingTimeSlot> timeSlot = bookingTimeSlotRepository.findById(bookingRequest.getTimeSlotId());
        //Check if the time slot exists
        if (timeSlot.isPresent()) {
            //Check if the time slot is already full or not
            if (
                    bookingRepository.findByBookingTimeSlotTimeSlotId(bookingRequest.getTimeSlotId()).size()
                    >= timeSlot.get().getMaxBookingCount()
            ) {
                return null;
            }
            Booking newBooking = new Booking(
                    timeSlot.get(),
                    LocalDateTime.now(),
                    bookingRequest.getUserId()
            );
            bookingRepository.save(newBooking);
            return newBooking;
        }
        // If the time slot doesn't exist, return null
        return null;
    }

    @Transactional
    public List<Booking> createBulkBooking(CreateBulkBookingRequest bookingRequest) {
        Optional<BookingTimeSlot> timeSlot = bookingTimeSlotRepository.findById(bookingRequest.getTimeSlotId());
        //Check if the time slot exists
        if (timeSlot.isPresent()) {
            //Check if the time slot is already full or not
            if (
                    bookingRepository.findByBookingTimeSlotTimeSlotId(bookingRequest.getTimeSlotId()).size()
                            + bookingRequest.getTicketCount()
                            > timeSlot.get().getMaxBookingCount()
                    &&
                            bookingRequest.getTicketCount() > 0
            ) {
                return null;
            }
            //If it's not, make bookings based on the count given
            List<Booking> bookingList = new ArrayList<>();
            for (int i = 0; i < bookingRequest.getTicketCount(); i++) {
                Booking newBooking = new Booking(
                        timeSlot.get(),
                        LocalDateTime.now(),
                        bookingRequest.getUserId()
                );
                bookingList.add(newBooking);
                bookingRepository.save(newBooking);
            }
            return bookingList;
        }
        // If the time slot doesn't exist, return null
        return null;
    }

    public Booking updateBooking(UpdateBookingRequest bookingRequest) {

        Optional<Booking> booking = bookingRepository.findById(bookingRequest.getBookingId());
        Optional<BookingTimeSlot> timeSlot = bookingTimeSlotRepository.findById(bookingRequest.getTimeSlotId());
        //Check if the booking and time slot exists
        if (booking.isPresent() && timeSlot.isPresent()) {
            Booking newBooking = new Booking(
                    timeSlot.get(),
                    bookingRequest.getBookingTime(),
                    bookingRequest.getUserId()
            );
            newBooking.setBookingId(bookingRequest.getBookingId());
            bookingRepository.save(newBooking);
            return newBooking;
        }
        // If the booking or time slot don't exist, return null
        return null;
    }

    @Transactional
    public void deleteBooking(Integer bookingId) {
        bookingRepository.deleteById(bookingId);
    }

}
