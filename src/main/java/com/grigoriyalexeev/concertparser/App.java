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
    public static final String MARIINSKY_BASE_URL =
            "http://www.mariinsky.ru/en/playbill/playbill/";
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
    public static final String MARIINSKY_EXAMPLE =
            "</div>" +
                "<div class=\"row c_theatre2 c_chamber_halls c_concert spec_row\" itemscope itemtype=\"http://data-vocabulary.org/Event\">" +
                    "<div class=\"col-md-3 place_col\">" +
                        "<div class=\"place\">" +
                            "<span itemprop=\"location\">The Prokofiev Hall (Mariinsky-II)</span>" +
                        "</div>" +
                        "<div class=\"t_button\"></div>" +
                        "<div class=\"time2\">19:00</div>" +
                    "</div>" +
                    "<div class=\"col-md-2 time_cnt\"><div class=\"time\"><time itemprop=\"startDate\" datetime=\"2015-02-21T19:00:00+03:00\">19:00</time></div></div>" +
                    "<div class=\"col-md-7 inf\">" +
                        "<div class=\"premiera\"></div>" +
                        "<div class=\"status\">Music Hour</div>" +
                        "<div class=\"spec_name\">" +
                            "<a href=\"/en/playbill/playbill/2015/2/21/8_1900/\" itemprop=\"url\">" +
                                "<span itemprop=\"summary\">Bach. Shuman. Glinka. Prokofiev</span>" +
                            "</a>" +
                        "</div>" +
                    "<div class=\"descr\">Natalia&nbsp;Baikova (cello), Alexander&nbsp;Alexeyev (double basses), Alisa&nbsp;Bratslavskaya, Dmitry&nbsp;Yefimov (piano)</div>" +
                "</div>" +
            "</div>";
    public static final String PHILHARMONIA_EXAMPLE =
            "<div class='afisha_list_item zal347 event_type_0 ta afisha_list_item_done1'  title=\"St. Petersburg Mozartiana\"" +
            "                     data-scope-url=\"/en/afisha/52824/\" data-scope-title=\"St. Petersburg Mozartiana\">" +
            "    <div class='td afisha_li_data'>" +
            "        <div class='date_block ta'>" +
            "            <div class='td date_block_d'>" +
            "                <div class='date_day'>04</div>" +
            "            </div>" +
            "            <div class='td date_dwh'>" +
            "                <div class='date_dw'>" +
            "                    <span class='date_dw_m'>March</span>, <span class='date_y'>2015</span>" +
            "                    <div class=''><span class='date_h'>07:00 pm</span>, <span class='date_dw_w dw3'>Wed</span></div>" +
            "                </div>" +
            "                <div class='afisha_element_z'><div></div><a href=\"/about/roadmap/\" target=\"_blank\">Small Hall</a></div>" +
            "            </div>" +
            "        </div>" +
            "    </div>" +
            "    <div class='td afisha_li_img'>" +
            "        <div class='mer_item_img_o'>" +
            "            <img class=\"mer_item_img hand\" onclick=\"location.href='/en/afisha/52824/'\" src=\"/upload/iblock/c98/beethoven.gif\">" +
            "            <div class='shad'></div>" +
            "        </div>" +
            "    </div>" +
            "    <div class='td afisha_li_descr'>" +
            "        <a class=\"mer_item_title hand\"  href=\"/en/afisha/52824/\"  >St. Petersburg Mozartiana</a>" +
            "        <a class=\"mer_item_sub_title hand\"  href=\"/en/afisha/52824/\" ></a>" +
            "        <div class='mer_item_list_colls'>" +
            "           <div class=\"mer_item_list_coll\">" +
            "               <div class=\"mer_item_list_coll_title\">" +
            "                   <i></i>" +
            "                   St. Petersburg State Academic Symphony Orchestra" +
            "               </div>" +
            "               <div class=\"mer_item_list_coll_pers\"></div>" +
            "           </div>" +
            "        </div>" +
            "        <div class='mer_item_list_persmain'>" +
            "            Conductor - <a class=\"mer_item_list_persmain_item_t\" href=\"/en/persons/biography/39293/\">Peter Feranec</a><br><a class=\"mer_item_list_persmain_item_t\" href=\"/en/persons/biography/5515/\">M&#237;ce&#225;l O'Rourke</a> - piano" +
            "        </div>" +
            "        <div class='mer_item_list_persmain'></div>" +
            "        <div class='mer_item_list_progr'>" +
            "            <b class=\"mer_item_list_title\">Mozart</b>, <b class=\"mer_item_list_title\">Beethoven</b>" +
            "        </div>" +
            "    </div>" +
            "    <div class='td afisha_li_function'></div>" +
            "</div>";


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
