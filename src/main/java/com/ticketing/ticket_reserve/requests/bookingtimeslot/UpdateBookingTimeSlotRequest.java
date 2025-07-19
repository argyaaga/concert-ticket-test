package com.ticketing.ticket_reserve.requests.bookingtimeslot;

import java.time.LocalDateTime;

public class UpdateBookingTimeSlotRequest {
    private Integer timeSlotId;
    private Integer concertId;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Integer maxBookingCount;

    public UpdateBookingTimeSlotRequest(
            Integer timeSlotId, Integer concertId, String openTime,
            String closeTime, Integer maxBookingCount) {
        this.timeSlotId = timeSlotId;
        this.concertId = concertId;
        this.openTime = LocalDateTime.parse(openTime);
        this.closeTime = LocalDateTime.parse(closeTime);
        this.maxBookingCount = maxBookingCount;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Integer getConcertId() {
        return concertId;
    }

    public void setConcertId(Integer concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = LocalDateTime.parse(openTime);
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = LocalDateTime.parse(closeTime);
    }

    public Integer getMaxBookingCount() {
        return maxBookingCount;
    }

    public void setMaxBookingCount(Integer maxBookingCount) {
        this.maxBookingCount = maxBookingCount;
    }
}
