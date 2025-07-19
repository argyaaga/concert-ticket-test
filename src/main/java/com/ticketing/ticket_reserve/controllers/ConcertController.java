package com.ticketing.ticket_reserve.controllers;

import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.requests.concert.CreateConcertRequest;
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

    @GetMapping("/{concertId}")
    public ResponseEntity<Concert> getConcert(@PathVariable Integer concertId) {
        Concert result = concertService.getConcert(concertId);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{concertId}")
    public ResponseEntity<String> deleteConcert(@PathVariable Integer concertId) {
        concertService.deleteConcert(concertId);
        return new ResponseEntity<>("Concert with ID "+concertId+" has been deleted", HttpStatus.OK);
    }

    @GetMapping
    public List<Concert> getConcerts() {
        return concertService.getConcerts();
    }

    @GetMapping("/upcoming")
    public List<Concert> getUpcomingConcerts() {
        return concertService.getUpcomingConcerts();
    }

    @PostMapping("/update")
    public ResponseEntity<Concert> updateConcert(@RequestBody Concert concert) {
        Concert result = concertService.updateConcert(concert);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Concert> createConcert(@RequestBody CreateConcertRequest concertRequest) {
        Concert createdConcert = concertService.createConcert(concertRequest);
        if (createdConcert == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdConcert, HttpStatus.CREATED);
    }
}
