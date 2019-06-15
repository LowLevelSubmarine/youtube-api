package com.lowlevelsubmarine.youtube_api.testing;

import com.lowlevelsubmarine.youtube_api.SearchResult;
import com.lowlevelsubmarine.youtube_api.Video;
import com.lowlevelsubmarine.youtube_api.YouTubeApi;

import java.util.LinkedList;

public class Test {

    private final YouTubeApi api;

    public static void main(String[] args) {
        new Test();
    }

    private Test() {
        this.api = new YouTubeApi();
        System.out.println("Die Toten Hosen");
        this.api.search("lana del rey born to die", this::onSearch, null);
    }

    private void onSearch(LinkedList<SearchResult> searchResults) {
        for (SearchResult result : searchResults) {
            System.out.println(result.getTitle());
        }
        this.api.getVideo(searchResults.get(0).getId(), this::onVideo, null);
    }

    private void onVideo(Video video) {
        System.out.println(video.getTitle());
    }

}
