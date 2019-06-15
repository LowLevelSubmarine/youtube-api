package com.lowlevelsubmarine.youtube_api;

import com.lowlevelsubmarine.youtube_api.hooks.FailHook;
import com.lowlevelsubmarine.youtube_api.hooks.SuccessHook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.LinkedList;

public class YouTubeApi {

    private final String userAgent;

    public YouTubeApi(String userAgent) {
        this.userAgent = userAgent;
    }

    public YouTubeApi() {
        this.userAgent = "API";
    }

    public void search(String query, SuccessHook<LinkedList<SearchResult>> successHook, FailHook<Void> failHook) {
        new SearchScraper(this, query, successHook, failHook).start();
    }

    public void getVideo(String id, SuccessHook<Video> successHook, FailHook<String> failHook) {
        new VideoScraper(this, id, successHook, failHook).start();
    }

    Connection getConnection(String url) {
        return Jsoup.connect(url).userAgent(this.userAgent).header("Accept-Language", "en-US");
    }


}
