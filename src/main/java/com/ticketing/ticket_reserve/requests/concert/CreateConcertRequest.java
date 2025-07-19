package com.ticketing.ticket_reserve.requests.concert;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CreateConcertRequest {
    private String concertName;
    private String concertDescription;
    private String concertTime;

    public CreateConcertRequest(String concertName, String concertDescription, String concertTime) {
        this.concertName = concertName;
        this.concertDescription = concertDescription;
        this.concertTime = concertTime;
    }

    public String getConcertName() {
        return concertName;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public String getConcertDescription() {
        return concertDescription;
    }

    public void setConcertDescription(String concertDescription) {
        this.concertDescription = concertDescription;
    }

    public String getConcertTime() {
        return concertTime;
    }

    public LocalDateTime getConcertDateTime() {
        try {
            return LocalDateTime.parse(concertTime);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void setConcertTime(String concertTime) {
        this.concertTime = concertTime;
    }
}
