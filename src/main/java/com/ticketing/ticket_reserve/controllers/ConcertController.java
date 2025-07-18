package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.requests.concert.CreateConcertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ticketing.ticket_reserve.services.ConcertService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @Autowired
    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping
    public List<Concert> getConcerts() {
        return concertService.getConcerts();
    }

    @GetMapping("/upcoming")
    public List<Concert> getUpcomingConcerts() {
        return concertService.getUpcomingConcerts();
    }

    @GetMapping("/update")
    public ResponseEntity<Concert> updateConcert(@RequestBody Concert concert) {
        concertService.updateConcert(concert);
        return new ResponseEntity<>(concert, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Concert> createConcert(@RequestBody CreateConcertRequest concertRequest) {
        Concert createdConcert = concertService.createConcert(concertRequest);
        return new ResponseEntity<>(createdConcert, HttpStatus.CREATED);
    }
}
