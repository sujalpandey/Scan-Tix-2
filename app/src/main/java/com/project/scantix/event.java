package com.project.scantix;

public class event {
    private String eventName;
    private String uid;
    private String eventDescription;

    // Empty constructor required for Firestore
    public event() {
    }

    public event(String eventName, String eventDescription, String uid) {
        this.eventName = eventName;
        this.uid = uid;
        this.eventDescription = eventDescription;
    }

    public String getEventName() {
        return eventName;
    }

    public String getUid() {
        return uid;
    }

    public String getEventDescription() {
        return eventDescription;
    }
}
