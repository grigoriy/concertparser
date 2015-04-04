package com.grigoriyalexeev.concertparser.service.extractor.impl;

import com.grigoriyalexeev.concertparser.model.Event;
import com.grigoriyalexeev.concertparser.service.extractor.Extractor;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PhilharmoniaExtractor implements Extractor {
    public static final String ROOT = "div.mer_item_list_progr";

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
        String summary = "";
        for (Element child : element.children()) {
            summary += child.text() + ", ";
        }
        return new Event("PHILHARMONIA", DateTime.now().plusYears(100), summary);
    }
}
