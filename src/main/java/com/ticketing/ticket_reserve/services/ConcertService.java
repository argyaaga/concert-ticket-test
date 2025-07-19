package com.ticketing.ticket_reserve.services;

import com.ticketing.ticket_reserve.entities.Concert;
import com.ticketing.ticket_reserve.requests.concert.CreateConcertRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ticketing.ticket_reserve.repositories.ConcertRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConcertService {

    private final ConcertRepository concertRepository;

    @Autowired
    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public Concert getConcert(Integer concertId) {
        Optional<Concert> concert = concertRepository.findById(concertId);
        return concert.orElse(null);
    }

    public List<Concert> getConcerts() { return concertRepository.findAll();}

    public List<Concert> getUpcomingConcerts() {
        return concertRepository.findAll().stream()
                //This filters for only future concerts, so concerts in the past cannot show up.
                .filter(concert -> concert.getConcertTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public Concert createConcert(CreateConcertRequest concertRequest) {
        //If time is invalid, it will return null
        if (concertRequest.getConcertDateTime() == null) {
            return null;
        }
        Concert newConcert = new Concert(
                concertRequest.getConcertName(),
                concertRequest.getConcertDescription(),
                concertRequest.getConcertDateTime()
        );
        concertRepository.save(newConcert);
        return newConcert;
    }

    public Concert updateConcert(Concert concert) {
        //Technically the previous function can be used for this, but it may be useful to know
        //  if the concert to be updated exists or not.
        Optional<Concert> oldConcert = concertRepository.findById(concert.getConcertId());

        if (oldConcert.isPresent()) {
            concertRepository.save(concert);
            return concert;
        }
        // If it doesn't exist, return null
        return null;
    }

    @Transactional
    public void deleteConcert(Integer concertId) {
        concertRepository.deleteById(concertId);
    }

}
