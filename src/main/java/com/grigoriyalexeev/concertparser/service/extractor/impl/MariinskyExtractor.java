package com.grigoriyalexeev.concertparser.service.extractor.impl;

import com.grigoriyalexeev.concertparser.model.Event;
import com.grigoriyalexeev.concertparser.service.extractor.Extractor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MariinskyExtractor implements Extractor {
    public static final String ROOT = "div.spec_row";
    public static final String TIMESTAMP = "time";
    public static final String TIMESTAMP_ATTRIBUTE = "datetime";
    public static final String LOCATION = "span[itemprop=location]";
    public static final String DESCRIPTION = "span[itemprop=summary]";
    public static final String DESCRIPTION_2 = "div.descr";

    @Override
    public List<Event> extract(Document htmlDocument) {
        Elements elements = htmlDocument.select(ROOT);
        List<Event> events = new ArrayList<>();
        for (Element element : elements) {
            events.add(extractSingle(element));
        }
        return events;
    }

    private Event extractSingle(Element element) {
        DateTime dateTime = DateTime.parse(element.select(TIMESTAMP).attr(TIMESTAMP_ATTRIBUTE).replace("T", " ").substring(0, 19),
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        String location = "MARIINSKY: " + element.select(LOCATION).text();
        String description = element.select(DESCRIPTION).text() + ". " + element.select(DESCRIPTION_2).text();
        return new Event(location, dateTime, description);
    }
}
