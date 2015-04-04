package com.grigoriyalexeev.concertparser.service.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Downloader {
    public static final int TIMEOUT_MILLIS = 5000;
    private String url;
    private Map<String, String> queryParams = new HashMap<>();
    private Map<QueryParam, String> genericToCustomKeys = new HashMap<>();

    public Downloader(String url, Map<QueryParam, String> genericToCustomQueryParamNames,
                      Map<QueryParam, String> genericQueryParamNamesToValues) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        }
        this.url = url;

        if (genericToCustomQueryParamNames != null) {
            genericToCustomKeys.putAll(genericToCustomQueryParamNames);
        } else {
            return;
        }

        if (genericQueryParamNamesToValues != null) {
            for (Map.Entry<QueryParam, String> queryParam : genericQueryParamNamesToValues.entrySet()) {
                addOrUpdateQueryParam(queryParam.getKey(), queryParam.getValue());
            }
        }
    }

    public Document download() throws IOException {
        if (queryParams.isEmpty()) {
            return Jsoup.connect(url).timeout(TIMEOUT_MILLIS).get();
        }
        Document result = Jsoup.connect(url).timeout(TIMEOUT_MILLIS).data(queryParams).get();
        return result;
    }

    public Downloader addOrUpdateQueryParam(QueryParam key, String value) {
        if (genericToCustomKeys.containsKey(key)) {
            queryParams.put(genericToCustomKeys.get(key), value);
            return this;
        } else {
            throw new IllegalArgumentException(String.format("'%s' parameter is not supported for this website", key));
        }
    }
}
