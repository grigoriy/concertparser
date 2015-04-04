package com.grigoriyalexeev.concertparser;

import com.grigoriyalexeev.concertparser.model.Event;
import com.grigoriyalexeev.concertparser.service.EventService;
import com.grigoriyalexeev.concertparser.service.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class App  {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        EventService eventService = applicationContext.getBean(EventService.class);

        Filter filter = new Filter().addFilter(Filter.EventField.DESCRIPTION,
                ".*?(Bach|Mozart|Beethoven|Wagner|Mahler|Villa[- ]Lobos|Sc?hostakovich|[Gg]uitar).*?");
        List<Event> events = eventService.getEvents(filter);

        for (Event event : events) {
            System.out.println(event);
        }
    }
}
