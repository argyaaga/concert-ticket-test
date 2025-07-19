package com.ticketing.ticket_reserve.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "concerts")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer concertId;
    private String concertName;
    private String concertDescription;
    private LocalDateTime concertTime;

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<BookingTimeSlot> bookingTimeSlotList = new ArrayList<>();

    public Concert(){}

    public Concert(String concertName, String concertDescription, LocalDateTime concertTime) {
        this.concertName = concertName;
        this.concertDescription = concertDescription;
        this.concertTime = concertTime;
    }

    public Integer getConcertId() {
        return concertId;
    }

    public String getConcertName() {
        return concertName;
    }

    public String getConcertDescription() {
        return concertDescription;
    }

    public LocalDateTime getConcertTime() {
        return concertTime;
    }

    public void setConcertId(Integer concertId) {
        this.concertId = concertId;
    }

    public void setConcertName(String concertName) {
        this.concertName = concertName;
    }

    public void setConcertDescription(String concertDescription) {
        this.concertDescription = concertDescription;
    }

    public void setConcertTime(LocalDateTime concertTime) {
        this.concertTime = concertTime;
    }
}
