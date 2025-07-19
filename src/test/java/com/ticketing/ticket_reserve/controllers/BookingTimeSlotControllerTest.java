package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.BaseIntegrationTest;
import com.ticketing.ticket_reserve.TicketReserveApplication;
import com.ticketing.ticket_reserve.entities.Booking;
import com.ticketing.ticket_reserve.entities.BookingTimeSlot;
import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.repositories.BookingRepository;
import com.ticketing.ticket_reserve.repositories.BookingTimeSlotRepository;
import com.ticketing.ticket_reserve.repositories.ConcertRepository;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.CreateBookingTimeSlotRequest;
import com.ticketing.ticket_reserve.requests.bookingtimeslot.UpdateBookingTimeSlotRequest;
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
public class BookingTimeSlotControllerTest extends BaseIntegrationTest {

    private static final String ENDPOINT = "/api/timeslot";

    @Autowired private BookingTimeSlotRepository timeSlotRepository;
    @Autowired private ConcertRepository concertRepository;
    @Autowired private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        timeSlotRepository.deleteAll();
        concertRepository.deleteAll();
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

    void createConcerts(int count) {
        for (int i = 1; i < count+1; i++) {
            Concert newConcert = new Concert(
                    "Concert "+i,
                    "Description "+i,
                    LocalDateTime.now().plusYears(2+count)
            );
            concertRepository.save(newConcert);
        }
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

    @Test
    void createTimeSlot_success() {
        createConcerts(3);
        CreateBookingTimeSlotRequest bookingTimeSlotRequest = new CreateBookingTimeSlotRequest(
                concertRepository.findAll().getFirst().getConcertId(),
                LocalDateTime.now().plusDays(1).toString(),
                LocalDateTime.now().plusDays(2).toString(),
                5
        );
        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        bookingTimeSlotRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(responseEntity.getBody().getConcert().getConcertId(),
                concertRepository.findAll().getFirst().getConcertId());
        Assertions.assertEquals(responseEntity.getBody().getMaxBookingCount(), 5);
    }

    @Test
    void createTimeSlot_failure_concert_doesnt_exist() {
        createConcerts(3);
        CreateBookingTimeSlotRequest bookingTimeSlotRequest = new CreateBookingTimeSlotRequest(
                1000000000,
                LocalDateTime.now().plusDays(1).toString(),
                LocalDateTime.now().plusDays(2).toString(),
                5
        );
        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        bookingTimeSlotRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void createTimeSlot_failure_past_concert_time() {
        createConcerts(3);
        CreateBookingTimeSlotRequest bookingTimeSlotRequest = new CreateBookingTimeSlotRequest(
                concertRepository.findAll().getFirst().getConcertId(),
                LocalDateTime.now().plusYears(500).toString(),
                LocalDateTime.now().plusYears(500).toString(),
                5
        );
        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        bookingTimeSlotRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getTimeSlot_success() {
        createConcerts(3);
        createTimeSlots(3);

        BookingTimeSlot testTimeSlot = timeSlotRepository.findAll().getFirst();

        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/"+testTimeSlot.getTimeSlotId())
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().getTimeSlotId(), testTimeSlot.getTimeSlotId());
    }

    @Test
    void getTimeSlot_failure() {
        createConcerts(3);
        createTimeSlots(3);

        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/9932139")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void getTimeSlots_success() {
        createConcerts(3);
        createTimeSlots(3);

        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT)
                );

        ResponseEntity<List<BookingTimeSlot>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getBody().size(), 3);
    }

    @Test
    void getUpcomingSlots_success() {
        createConcerts(2);
        Concert newConcert = new Concert(
                "Concert",
                "Description",
                LocalDateTime.now().plusYears(2)
        );
        concertRepository.save(newConcert);
        BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                newConcert,
                LocalDateTime.now().minusYears(2),
                LocalDateTime.now().minusDays(2),
                5
        );
        timeSlotRepository.save(newTimeSlot);
        createTimeSlots(2);

        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/upcoming")
                );

        ResponseEntity<List<BookingTimeSlot>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getBody().size(), 2);
    }

    @Test
    void getAvailableSlots_success() {
        Concert newConcert = new Concert(
                "Concert",
                "Description",
                LocalDateTime.now().plusYears(2)
        );
        concertRepository.save(newConcert);
        for (int i = 0; i < 3; i++) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    newConcert,
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().plusDays(2),
                    5
            );
            timeSlotRepository.save(newTimeSlot);
        }
        createBookings(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                5
        );
        for (int i = 0; i < 3; i++) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    newConcert,
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1),
                    5
            );
            timeSlotRepository.save(newTimeSlot);
        }
        for (int i = 0; i < 3; i++) {
            BookingTimeSlot newTimeSlot = new BookingTimeSlot(
                    newConcert,
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(2),
                    5
            );
            timeSlotRepository.save(newTimeSlot);
        }

        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/available")
                );

        ResponseEntity<List<BookingTimeSlot>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getBody().size(), 2);
    }

    @Test
    void updateTimeSlot_success() {
        createConcerts(3);
        createTimeSlots(3);
        String tempTime = LocalDateTime.now().plusDays(200).toString();
        UpdateBookingTimeSlotRequest bookingTimeSlotRequest = new UpdateBookingTimeSlotRequest(
                timeSlotRepository.findAll().getFirst().getTimeSlotId(),
                concertRepository.findAll().getFirst().getConcertId(),
                LocalDateTime.now().minusDays(5).toString(),
                tempTime,
                100
        );
        RequestEntity<UpdateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        bookingTimeSlotRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().getConcert().getConcertId(),
                concertRepository.findAll().getFirst().getConcertId());
        Assertions.assertEquals(responseEntity.getBody().getMaxBookingCount(), 100);
        Assertions.assertEquals(responseEntity.getBody().getCloseTime().toString(), tempTime);
    }

    @Test
    void updateTimeSlot_failure() {
        createConcerts(3);
        createTimeSlots(3);
        String tempTime = LocalDateTime.now().plusDays(200).toString();
        UpdateBookingTimeSlotRequest bookingTimeSlotRequest = new UpdateBookingTimeSlotRequest(
                99999,
                concertRepository.findAll().getFirst().getConcertId(),
                LocalDateTime.now().minusDays(5).toString(),
                tempTime,
                100
        );
        RequestEntity<UpdateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        bookingTimeSlotRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<BookingTimeSlot> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteTimeSlot_success() {
        createConcerts(3);
        createTimeSlots(3);
        BookingTimeSlot testTimeSlot = timeSlotRepository.findAll().getFirst();
        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/"+testTimeSlot.getTimeSlotId())
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(timeSlotRepository.findAll().size(), 2);
    }

    @Test
    void deleteTimeSlot_failure() {
        createConcerts(3);
        createTimeSlots(3);
        BookingTimeSlot testTimeSlot = timeSlotRepository.findAll().getFirst();
        RequestEntity<CreateBookingTimeSlotRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/1231354")
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(timeSlotRepository.findAll().size(), 3);
    }

}
