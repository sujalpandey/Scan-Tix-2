package com.project.scantix;

import java.util.Date;

public class Ticket {
    private String eventName;
    private String eventDescription;
    private Date eventDate;


    public Ticket(String eventName, String eventDescription, Date eventDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;

    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Date getEventDate() {
        return eventDate;
    }


}
