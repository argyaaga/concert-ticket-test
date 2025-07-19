package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.BaseIntegrationTest;
import com.ticketing.ticket_reserve.TicketReserveApplication;
import com.ticketing.ticket_reserve.entities.Booking;
import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.repositories.BookingRepository;
import com.ticketing.ticket_reserve.repositories.BookingTimeSlotRepository;
import com.ticketing.ticket_reserve.repositories.ConcertRepository;
import com.ticketing.ticket_reserve.requests.booking.CreateBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.CreateBulkBookingRequest;
import com.ticketing.ticket_reserve.requests.booking.UpdateBookingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes ={TicketReserveApplication.class, ConcertControllerTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BookingControllerTest extends BaseIntegrationTest {

    private static final String ENDPOINT = "/api/booking";

    @Autowired private BookingTimeSlotRepository timeSlotRepository;
    @Autowired private ConcertRepository concertRepository;
    @Autowired private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        timeSlotRepository.deleteAll();
        concertRepository.deleteAll();
    }

    void createTimeSlots(int count) {
        Concert newConcert = new Concert(
                "Concert",
                "Description",
                LocalDateTime.now().plusYears(2+count)
        );
        concertRepository.save(newConcert);
        for (int i = 0; i < count; i++) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    newConcert,
                    LocalDateTime.now().plusDays(i),
                    LocalDateTime.now().plusDays(i+2),
                    5
            );
            timeSlotRepository.save(newTimeSlot);
        }
    }

    void createBookings(Integer timeSlotId, int count) {
        for (int i = 0; i < count; i++) {
            Booking newBooking = new Booking(
                    timeSlotRepository.findById(timeSlotId).get(),
                    LocalDateTime.now(),
                    100
            );
            bookingRepository.save(newBooking);
        }
    }

    @Test
    void createBooking_success() {
        createTimeSlots(2);
        CreateBookingRequest bookingRequest = new CreateBookingRequest(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                100
        );
        RequestEntity<CreateBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(responseEntity.getBody().getBookingTimeSlot().getTimeSlotId(),
                timeSlotRepository.findAll().getFirst().getTimeSlotId());
        Assertions.assertEquals(responseEntity.getBody().getUserId(), 100);

    }

    @Test
    void createBooking_failure_time_slot_doesnt_exist() {
        createTimeSlots(2);
        CreateBookingRequest bookingRequest = new CreateBookingRequest(
                999999,
                100
        );
        RequestEntity<CreateBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createBulkBooking_success() {
        createTimeSlots(2);
        CreateBulkBookingRequest bookingRequest = new CreateBulkBookingRequest(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                100,
                4
        );
        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create_bulk")
                );

        ResponseEntity<List<Booking>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(responseEntity.getBody().getFirst().getBookingTimeSlot().getTimeSlotId(),
                timeSlotRepository.findAll().getFirst().getTimeSlotId());
        Assertions.assertEquals(responseEntity.getBody().size(), 4);
        Assertions.assertEquals(responseEntity.getBody().getFirst().getUserId(), 100);
    }

    @Test
    void createBulkBooking_failure_time_slot_doesnt_exist() {
        createTimeSlots(2);
        CreateBulkBookingRequest bookingRequest = new CreateBulkBookingRequest(
                9999999,
                100,
                4
        );
        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create_bulk")
                );

        ResponseEntity<List<Booking>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void createBulkBooking_failure_not_enough_tickets_available() {
        createTimeSlots(2);
        CreateBulkBookingRequest bookingRequest = new CreateBulkBookingRequest(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                100,
                1000
        );
        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create_bulk")
                );

        ResponseEntity<List<Booking>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    void getBooking_success() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                3
        );

        Booking testBooking = bookingRepository.findAll().getFirst();

        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/"+testBooking.getBookingId())
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().getBookingId(), testBooking.getBookingId());
    }

    @Test
    void getBooking_failure() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                3
        );
        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/102392")
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBooking_success() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                3
        );

        Booking testBooking = bookingRepository.findAll().getFirst();

        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/"+testBooking.getBookingId())
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(bookingRepository.findAll().size(), 2);
    }

    @Test
    void deleteBooking_failure() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                3
        );

        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/123129")
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(bookingRepository.findAll().size(), 3);
    }

    @Test
    void getBookings_success() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                3
        );

        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT)
                );

        ResponseEntity<List<Booking>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().size(), 3);
    }

    @Test
    void getTimeSlotBookings_success() {
        createTimeSlots(3);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                4
        );
        createBookings(
                timeSlotRepository.findAll().getLast().getTimeSlotId(),
                3
        );

        RequestEntity<CreateBulkBookingRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/timeslot/"+timeSlotRepository.findAll().getFirst().getTimeSlotId())
                );

        ResponseEntity<List<Booking>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().size(), 4);
    }

    @Test
    void updateBooking_success() {
        createTimeSlots(2);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                4
        );
        UpdateBookingRequest bookingRequest = new UpdateBookingRequest(
                bookingRepository.findAll().getFirst().getBookingId(),
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                LocalDateTime.now().plusDays(1).toString(),
                122
        );
        RequestEntity<UpdateBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().getBookingTimeSlot().getTimeSlotId(),
                timeSlotRepository.findAll().getFirst().getTimeSlotId());
        Assertions.assertEquals(responseEntity.getBody().getUserId(), 122);

    }

    @Test
    void updateBooking_failure() {
        createTimeSlots(2);
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                4
        );
        UpdateBookingRequest bookingRequest = new UpdateBookingRequest(
                99999,
                99999,
                LocalDateTime.now().plusDays(1).toString(),
                122
        );
        RequestEntity<UpdateBookingRequest> requestEntity =
                new RequestEntity<>(
                        bookingRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<Booking> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }

}
