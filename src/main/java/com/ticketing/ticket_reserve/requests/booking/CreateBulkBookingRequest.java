package com.ticketing.ticket_reserve.requests.booking;

public class CreateBulkBookingRequest {
    private Integer timeSlotId;
    private Integer userId;
    private Integer ticketCount;

    public CreateBulkBookingRequest(Integer timeSlotId, Integer userId, Integer ticketCount) {
        this.timeSlotId = timeSlotId;
        this.userId = userId;
        this.ticketCount = ticketCount;
    }

    public Integer getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Integer timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }
}
