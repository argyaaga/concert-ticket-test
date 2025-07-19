package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.BaseIntegrationTest;
import com.ticketing.ticket_reserve.TicketReserveApplication;
import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.repositories.ConcertRepository;
import com.ticketing.ticket_reserve.requests.concert.CreateConcertRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class ConcertControllerTest extends BaseIntegrationTest {

    private static final String ENDPOINT = "/api/concert";

    @Autowired private ConcertRepository concertRepository;

    @BeforeEach
    void setUp() {
        concertRepository.deleteAll();
    }

    void createConcerts(int count) {
        for (int i = 1; i < count+1; i++) {
            Concert newConcert = new Concert(
                    "Concert "+i,
                    "Description "+i,
                    LocalDateTime.now()
            );
            concertRepository.save(newConcert);
        }
    }

    @Test
    void createConcert_success() {
        CreateConcertRequest concertRequest = new CreateConcertRequest(
                "Concert 1",
                "Description 1",
                "2026-01-08T04:05:06"
        );
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        concertRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals(responseEntity.getBody().getConcertName(), "Concert 1");
        Assertions.assertEquals(responseEntity.getBody().getConcertDescription(), "Description 1");
        Assertions.assertEquals(responseEntity.getBody().getConcertTime(), LocalDateTime.parse("2026-01-08T04:05:06"));
    }

    @Test
    void createConcert_failure() {
        CreateConcertRequest concertRequest = new CreateConcertRequest(
                "Concert 1",
                "Description 1",
                "WRONG"
        );
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        concertRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/create")
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    void getConcerts_success() {
        createConcerts(3);

        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT)
                );

        ResponseEntity<List<Concert>> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getBody().size(), 3);
    }

    @Test
    void getConcert_success() {
        createConcerts(3);
        Concert testConcert = concertRepository.findAll().getFirst();
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/"+testConcert.getConcertId())
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getBody().getConcertId(), testConcert.getConcertId());
    }

    @Test
    void getConcert_failure() {
        createConcerts(3);
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.GET,
                        URI.create(ENDPOINT+"/100")
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteConcert_success() {
        createConcerts(3);
        Concert testConcert = concertRepository.findAll().getFirst();
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/"+testConcert.getConcertId())
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(concertRepository.findAll().size(), 2);
    }

    @Test
    void deleteConcert_failure() {
        createConcerts(3);
        RequestEntity<CreateConcertRequest> requestEntity =
                new RequestEntity<>(
                        createHttpHeaders(),
                        HttpMethod.DELETE,
                        URI.create(ENDPOINT+"/123123")
                );

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(concertRepository.findAll().size(), 3);
    }

    @Test
    void updateConcert_success() {
        createConcerts(3);
        Concert testConcert = concertRepository.findAll().getFirst();
        Concert concertRequest = new Concert(
                "Concert 100",
                "Description 100",
                LocalDateTime.now()
        );
        concertRequest.setConcertId(testConcert.getConcertId());
        RequestEntity<Concert> requestEntity =
                new RequestEntity<>(
                        concertRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(responseEntity.getBody().getConcertId(), testConcert.getConcertId());
        Assertions.assertEquals(responseEntity.getBody().getConcertName(), "Concert 100");
        Assertions.assertEquals(responseEntity.getBody().getConcertDescription(), "Description 100");
    }

    @Test
    void updateConcert_failure() {
        createConcerts(3);
        Concert concertRequest = new Concert(
                "Concert 100",
                "Description 100",
                LocalDateTime.now()
        );
        concertRequest.setConcertId(10000000);
        RequestEntity<Concert> requestEntity =
                new RequestEntity<>(
                        concertRequest,
                        createHttpHeaders(),
                        HttpMethod.POST,
                        URI.create(ENDPOINT+"/update")
                );

        ResponseEntity<Concert> responseEntity =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>(){});

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

}
