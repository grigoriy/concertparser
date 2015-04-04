package com.grigoriyalexeev.concertparser.service;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.grigoriyalexeev.concertparser.model.Event;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private Predicate<CharSequence> dateTimePredicate = Predicates.alwaysTrue();
    private Predicate<CharSequence> locationPredicate = Predicates.alwaysTrue();
    private Predicate<CharSequence> descriptionPredicate = Predicates.alwaysTrue();

    public List<Event> filter(List<Event> events) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (dateTimePredicate.apply(event.getDateTime().toString())
                    && locationPredicate.apply(event.getLocation())
                    && descriptionPredicate.apply(event.getDescription())) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    public Filter addFilter(EventField eventField, String regex) {
        switch (eventField) {
            case DATE_TIME:
                this.dateTimePredicate = Predicates.containsPattern(regex);
            case LOCATION:
                this.locationPredicate = Predicates.containsPattern(regex);
            case DESCRIPTION:
                this.descriptionPredicate = Predicates.containsPattern(regex);
        }
        return this;
    }

    public enum EventField {
        DATE_TIME, LOCATION, DESCRIPTION
    }
}
