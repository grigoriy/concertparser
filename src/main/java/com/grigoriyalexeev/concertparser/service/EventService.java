package com.grigoriyalexeev.concertparser.service;

import com.grigoriyalexeev.concertparser.model.Event;
import com.grigoriyalexeev.concertparser.service.downloader.Downloader;
import com.grigoriyalexeev.concertparser.service.extractor.Extractor;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventService {
    private Map<Downloader, Extractor> downloaderExtractorMap;

    public EventService(Map<Downloader, Extractor> downloaderExtractorMap) {
        this.downloaderExtractorMap = downloaderExtractorMap;
    }

    public List<Event> getEvents(Filter filter) {
        List<Event> result = new ArrayList<>();
        for (Map.Entry<Downloader, Extractor> downloaderExtractorEntry : downloaderExtractorMap.entrySet()) {
            Document downloadedHtml = null;
            try {
                downloadedHtml = downloaderExtractorEntry.getKey().download();
            } catch (IOException e) {
                System.out.println("Could not download web page: " + e.getMessage());
            }

            List<Event> extractedEvents = (downloadedHtml != null)
                    ? downloaderExtractorEntry.getValue().extract(downloadedHtml)
                    : new ArrayList<Event>();

            List<Event> filteredEvents = filter.filter(extractedEvents);

            result.addAll(filteredEvents);
        }
        return result;
    }
}
