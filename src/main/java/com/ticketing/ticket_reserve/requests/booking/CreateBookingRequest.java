package com.ticketing.ticket_reserve.requests.booking;

import java.time.LocalDateTime;

public class CreateBookingRequest {
    private Integer timeSlotId;
    private Integer userId;

    public CreateBookingRequest(Integer timeSlotId, Integer userId) {
        this.timeSlotId = timeSlotId;
        this.userId = userId;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public Integer getUserId() {
        return userId;
    }
}
