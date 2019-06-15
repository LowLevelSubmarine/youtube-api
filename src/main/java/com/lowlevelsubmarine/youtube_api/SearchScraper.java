package com.lowlevelsubmarine.youtube_api;

import com.lowlevelsubmarine.youtube_api.hooks.FailHook;
import com.lowlevelsubmarine.youtube_api.hooks.SuccessHook;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.LinkedList;

public class SearchScraper extends Thread {

    private static final String REQUESET_URL = "http://www.youtube.com/results";
    private static final String QUERY_KEY = "search_query";
    private static final HashMap<String, LinkedList<SearchResult>> CACHE = new HashMap<>();

    private final YouTubeApi api;
    private final String query;
    private final SuccessHook<LinkedList<SearchResult>> successHook;
    private final FailHook<Void> failHook;

    SearchScraper(YouTubeApi api, String query, SuccessHook<LinkedList<SearchResult>> successHook, FailHook<Void> failHook) {
        this.api = api;
        this.query = query;
        this.successHook = successHook;
        this.failHook = failHook;
    }

    @Override
    public void run() {
        if (!CACHE.containsKey(this.query)) {
            try {
                Connection.Response response = this.api.getConnection(REQUESET_URL).data(QUERY_KEY, query).execute();
                Document document = response.parse();
                LinkedList<SearchResult> results = processPage(document);
                CACHE.put(this.query, results);
                this.successHook.onSuccess(results);
            } catch (Exception e) {
                e.printStackTrace();
                if (this.failHook != null) {
                    this.failHook.onFail(null);
                }
            }
        } else {
            this.successHook.onSuccess(CACHE.get(this.query));
        }
    }

    public LinkedList<SearchResult> processPage(Document document) {
        LinkedList<SearchResult> results = new LinkedList<>();
        for (Element element : document.select(".yt-lockup")) {
            if (!element.hasAttr("data-ad-impressions") && element.select(".standalone-ypc-badge-renderer-label").isEmpty()) {
                SearchResult result = convertResultElement(element);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        return results;
    }

    public SearchResult convertResultElement(Element element) {
        String id = element.attr("data-context-item-id");
        Element durationElement = element.select("[class^=video-time]").first();
        Element contentElement = element.select(".yt-lockup-content").first();
        Element descElement = element.getElementsByClass("yt-lockup-description").first();
        if (durationElement != null && contentElement != null && descElement != null && !id.isEmpty()) {
            SearchResult result = new SearchResult();
            result.setId(id);
            result.setTitle(contentElement.select(".yt-lockup-title > a").text());
            result.setUser(contentElement.select(".yt-lockup-byline > a").text());
            result.setDuration(durationTextToMillis(durationElement.text()));
            result.setDescriptionPreview(descElement.text());
            result.setAGen(AGenDescExtractor.isAGenDesc(result.getDescriptionPreview()));
            return result;
        }
        return null;
    }

    private long durationTextToMillis(String durationText) {
        int length = 0;
        for (String part : durationText.split("[:.]")) {
            length = length * 60 + Integer.valueOf(part);
        }
        return length * 1000L;
    }

}
