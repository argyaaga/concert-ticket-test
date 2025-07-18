package com.ticketing.ticket_reserve.requests.concert;

import java.time.LocalDateTime;

public class CreateConcertRequest {
    private String concertName;
    private String concertDescription;
    private LocalDateTime concertTime;

    public CreateConcertRequest(String concertName, String concertDescription, String concertTime) {
        this.concertName = concertName;
        this.concertDescription = concertDescription;
        this.concertTime = LocalDateTime.parse(concertTime);
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

    public LocalDateTime getConcertTime() {
        return concertTime;
    }

    public void setConcertTime(String concertTime) {
        this.concertTime = LocalDateTime.parse(concertTime);
    }
}
