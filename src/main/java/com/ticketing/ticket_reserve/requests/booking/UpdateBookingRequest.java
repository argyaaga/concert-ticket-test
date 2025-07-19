package com.ticketing.ticket_reserve.requests.booking;

import java.time.LocalDateTime;

public class UpdateBookingRequest {
    private Integer bookingId;
    private Integer timeSlotId;
    private LocalDateTime bookingTime;
    private Integer userId;

    public UpdateBookingRequest(Integer bookingId, Integer timeSlotId, String bookingTime, Integer userId) {
        this.bookingId = bookingId;
        this.timeSlotId = timeSlotId;
        this.bookingTime = LocalDateTime.parse(bookingTime);
        this.userId = userId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public Integer getUserId() {
        return userId;
    }
}
