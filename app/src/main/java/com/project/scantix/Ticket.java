package com.project.scantix;

import java.util.Date;

public class Ticket {
    private String eventName;
    private String eventDate;
    private String ticketNo;
    private String userName;
    private String eventVenue;


    public Ticket(String eventName, String eventDescription, String eventDate,String ticketNo,String userName,String eventVenue) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.ticketNo = ticketNo;
        this.userName = userName;
        this.eventVenue = eventVenue;


    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return event;
    }

    public String getEventDate() {
        return eventDate;
    }


}
