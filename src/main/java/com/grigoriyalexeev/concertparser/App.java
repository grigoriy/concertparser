package com.grigoriyalexeev.concertparser;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App  {
    public static final int MONTH_VALUE;
    public static final String MARIINSKY_BASE_URL = "http://www.mariinsky.ru/en/playbill/playbill/";
    public static final String MARIINSKY_YEAR_KEY = "year";
    public static final String MARIINSKY_MONTH_KEY = "month";
    public static final int MARIINSKY_END_OF_SEASON_MONTH = 6;
    public static final int MARIINSKY_YEAR_VALUE;
    public static final String MARIINSKY_ELEMENT = "span[itemprop=summary]";
    public static final String PHILHARMONIA_BASE_URL = "http://www.philharmonia.spb.ru/en/afisha/";
    public static final String PHILHARMONIA_YEAR_KEY = "ev_y";
    public static final String PHILHARMONIA_MONTH_KEY = "ev_m";
    public static final int PHILHARMONIA_END_OF_SEASON_MONTH = 7;
    public static final int PHILHARMONIA_YEAR_VALUE;
    public static final String PHILHARMONIA_ELEMENT = "div.mer_item_list_progr";

    static {
        final DateTime DATE = new DateTime();
        MARIINSKY_YEAR_VALUE = DATE.getYear();
        MONTH_VALUE = DATE.getMonthOfYear();
        PHILHARMONIA_YEAR_VALUE = MARIINSKY_YEAR_VALUE - 1;
    }


    public static void main( String[] args ) {
        List<String> mariinskyConcerts = getConcerts("mariinsky", MARIINSKY_BASE_URL, MARIINSKY_YEAR_KEY, MARIINSKY_MONTH_KEY,
                MARIINSKY_END_OF_SEASON_MONTH, MARIINSKY_ELEMENT, MARIINSKY_YEAR_VALUE);
        System.out.println("\nMARIINSKY\n");
        for (String concert : mariinskyConcerts) {
            System.out.println(concert);
        }

        List<String> philharmoniaConcerts = getConcerts("philharmonia", PHILHARMONIA_BASE_URL, PHILHARMONIA_YEAR_KEY,
                PHILHARMONIA_MONTH_KEY, PHILHARMONIA_END_OF_SEASON_MONTH, PHILHARMONIA_ELEMENT, PHILHARMONIA_YEAR_VALUE);
        System.out.println("\nPHILHARMONIA\n");
        for (String concert : philharmoniaConcerts) {
            System.out.println(concert);
        }
    }

    private static List<String> getConcerts(String concertHall, String baseUrl, String yearKey, String monthKey, int lastMonth, String element, int year) {
        List<String> result = new ArrayList<>();
        boolean mariinsky = "mariinsky".equals(concertHall);

        for (int i = MONTH_VALUE; i < lastMonth; i++) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put(yearKey, String.valueOf(year));
            queryParams.put(monthKey, String.valueOf(i));
            if (! mariinsky) {
                queryParams.put("ev_z", "346");
            }
            try {
                Document docWeb = Jsoup.connect(baseUrl)
                        .data(queryParams)
                        .timeout(4000)
                        .get();

                Elements summariesElements = docWeb.select(element);
                Pattern composersPattern = Pattern.compile(
                        ".*?(Bach|Mozart|Beethoven|Wagner|Mahler|Villa[- ]Lobos|Sc?hostakovich|[Gg]uitar).*?");
                for (Element summaryElement : summariesElements) {
                    String summary = "";
                    if (mariinsky) {
                        summary = summaryElement.text();
                    } else {
                        for (Element child : summaryElement.children()) {
                            summary += child.text() + ", ";
                        }
                    }
                    Matcher matcher = composersPattern.matcher(summary);
                    if (matcher.matches()) {
                        result.add(summary);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
