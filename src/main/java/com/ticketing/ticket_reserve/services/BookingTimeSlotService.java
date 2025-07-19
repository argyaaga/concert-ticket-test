package com.ticketing.ticket_reserve.services;

import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.repositories.BookingRepository;
import com.ticketing.ticket_reserve.repositories.BookingTimeSlotRepository;
import com.ticketing.ticket_reserve.repositories.ConcertRepository;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.CreateBookingTimeSlotRequest;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.UpdateBookingTimeSlotRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookingTimeSlotService {

    private final BookingTimeSlotRepository bookingTimeSlotRepository;
    private final ConcertRepository concertRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingTimeSlotService(BookingTimeSlotRepository bookingTimeSlotRepository, ConcertRepository concertRepository, BookingRepository bookingRepository) {
        this.bookingTimeSlotRepository = bookingTimeSlotRepository;
        this.concertRepository = concertRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<BookingTimeSlot> getTimeSlots() { return bookingTimeSlotRepository.findAll();}

    public BookingTimeSlot getTimeSlot(Integer timeSlotId) {
        Optional<BookingTimeSlot> timeSlot = bookingTimeSlotRepository.findById(timeSlotId);
        return timeSlot.orElse(null);
    }

    public List<BookingTimeSlot> getConcertTimeSlots(Integer concertId) {
        return bookingTimeSlotRepository.findByConcertConcertId(concertId);
    }

    public List<BookingTimeSlot> getUpcomingSlots() {
        return bookingTimeSlotRepository.findAll().stream()
                .filter(timeSlot -> timeSlot.getCloseTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public List<BookingTimeSlot> getAvailableSlots() {
        return bookingTimeSlotRepository.findAll().stream()
                .filter(bookingTimeSlot ->
                        //Check that the booking can be made right now
                        bookingTimeSlot.getOpenTime().isBefore(LocalDateTime.now())
                        &&
                        bookingTimeSlot.getCloseTime().isAfter(LocalDateTime.now())
                        && //Check that the booking is not already full in that time slot
                        (bookingRepository.findByBookingTimeSlotTimeSlotId(bookingTimeSlot.getTimeSlotId()).size()
                        < bookingTimeSlot.getMaxBookingCount()))
                .collect(Collectors.toList());
    }

    public BookingTimeSlot createTimeSlot(CreateBookingTimeSlotRequest timeSlotRequest) {

        Optional<Concert> concert = concertRepository.findById(timeSlotRequest.getConcertId());
        //Check if the concert exists
        if (concert.isPresent()) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    concert.get(),
                    timeSlotRequest.getOpenTime(),
                    timeSlotRequest.getCloseTime(),
                    timeSlotRequest.getMaxBookingCount()
            );
            bookingTimeSlotRepository.save(newTimeSlot);
            return newTimeSlot;
        }
        // If the concert doesn't exist, return null
        return null;
    }

    public BookingTimeSlot updateTimeSlot(UpdateBookingTimeSlotRequest timeSlotRequest) {

        Optional<BookingTimeSlot> timeSlot = bookingTimeSlotRepository.findById(timeSlotRequest.getTimeSlotId());
        Optional<Concert> concert = concertRepository.findById(timeSlotRequest.getConcertId());
        //Check if the concert and time slot exist
        if (concert.isPresent() && timeSlot.isPresent()) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    concert.get(),
                    timeSlotRequest.getOpenTime(),
                    timeSlotRequest.getCloseTime(),
                    timeSlotRequest.getMaxBookingCount()
            );
            newTimeSlot.setTimeSlotId(timeSlotRequest.getTimeSlotId());
            bookingTimeSlotRepository.save(newTimeSlot);
            return newTimeSlot;
        }
        // If the concert or timeslot don't exist, return null
        return null;
    }

    @Transactional
    public void deleteTimeSlot(Integer timeSlotId) {
        concertRepository.deleteById(timeSlotId);
    }

}
