package com.grigoriyalexeev.concertparser.service.extractor;

import com.grigoriyalexeev.concertparser.model.Event;
import org.jsoup.nodes.Document;

import java.util.List;

public interface Extractor {
    List<Event> extract(Document htmlDocument);
}
