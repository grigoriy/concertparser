package com.grigoriyalexeev.concertparser.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class Event {
    private String location;
    private DateTime dateTime;
    private String description;

    public Event(String location, DateTime dateTime, String description) {
        this.location = location;
        this.dateTime = dateTime;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Event{ "
                + location + "; "
                + DateTimeFormat.forPattern("yyyy-MMM-dd, EEE, HH:mm").print(dateTime)
                + "; " + description + " }";
    }
}
