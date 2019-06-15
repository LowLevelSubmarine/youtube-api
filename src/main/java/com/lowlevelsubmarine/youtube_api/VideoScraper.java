package com.lowlevelsubmarine.youtube_api;

import com.lowlevelsubmarine.youtube_api.hooks.FailHook;
import com.lowlevelsubmarine.youtube_api.hooks.SuccessHook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoScraper extends Thread {

    private static final Pattern CHANNEL_URL_PATTERN = Pattern.compile("/channel/([A-Za-z0-9_\\-]+)");

    private static final Pattern YT_DURATION_PATTERN_SECONDS = Pattern.compile("(\\d{1,2})S");
    private static final Pattern YT_DURATION_PATTERN_MINUTES = Pattern.compile("(\\d{1,2})M");
    private static final Pattern YT_DURATION_PATTERN_HOURS = Pattern.compile("(\\d{1,3})H");

    private static final String REQUEST_URL = "http://www.youtube.com/watch";
    private static final String VIDEO_KEY = "v";
    private static final HashMap<String, Video> CACHE = new HashMap<>();

    private final YouTubeApi api;
    private final String id;
    private final SuccessHook<Video> successHook;
    private final FailHook<String> failHook;

    public VideoScraper(YouTubeApi api, String id, SuccessHook<Video> successHook, FailHook<String> failHook) {
        this.api = api;
        this.id = id;
        this.successHook = successHook;
        this.failHook = failHook;
    }

    @Override
    public void run() {
        if (!CACHE.containsKey(this.id)) {
            try {
                Connection.Response response = this.api.getConnection(REQUEST_URL).data(VIDEO_KEY, this.id).execute();
                Document document = response.parse();
                Video video = processPage(document);
                CACHE.put(video.getId(), video);
                this.successHook.onSuccess(video);
            } catch (Exception e) {
                e.printStackTrace();
                if (this.failHook != null) {
                    this.failHook.onFail(this.id);
                }
            }
        } else {
            this.successHook.onSuccess(CACHE.get(this.id));
        }
    }

    private Video processPage(Document document) {
        Video video = new Video();
        video.setDescription(getText(document.getElementById("eow-description").html()));
        video.setId(this.id);
        video.setMusicInfos(parseMusicInfo(document));
        Element contentMeta = document.getElementById("watch7-content");
        for (Map.Entry<String, String> entry : JsoupTools.getMetaTags(contentMeta, "itemprop").entrySet()) {
            if (entry.getKey().equals("name")) {
                video.setTitle(entry.getValue());
            } else if (entry.getKey().equals("genre")) {
                for (Video.Category category : Video.Category.values()) {
                    if (category.getName().equals(entry.getValue())) {
                        video.setCategory(category);
                    }
                }
            } else if (entry.getKey().equals("duration")) {
                video.setDuration(ytDurationToMillis(entry.getValue()));
            }
        }
        Element userInfoLink = document.getElementsByClass("yt-user-info").first().getElementsByTag("a").first();
        video.setChannelName(userInfoLink.text());
        Matcher channelUrlMatcher = CHANNEL_URL_PATTERN.matcher(userInfoLink.attr("href"));
        if (channelUrlMatcher.matches()) {
            video.setChannelId(channelUrlMatcher.group(1));
        }
        try {
            AGenDescExtractor aGenDescExtractor = new AGenDescExtractor(video.getDescription());
            video.setAGenDesc(aGenDescExtractor.getAGenDesc());
        } catch (AGenDescExtractor.InvalidDescriptionException e) {}
        return video;
    }

    private MusicInfo[] parseMusicInfo(Document document) {
        LinkedList<MusicInfo> musicInfo = new LinkedList<>();
        for (Element element : document.getElementsByClass("watch-meta-item")) {
            String title = element.getElementsByClass("title").first().text();
            String content = element.getElementsByTag("ul").first().text();
            if (title.equals("Song")) {
                musicInfo.add(new MusicInfo());
                musicInfo.getLast().setTitle(content);
            } else if (title.equals("Artist")) {
                if (musicInfo.size() != 0 && musicInfo.getLast().getArtist() == null) {
                    musicInfo.getLast().setArtist(content);
                }
            } else if (title.equals("Album")) {
                if (musicInfo.size() != 0 && musicInfo.getLast().getAlbum() == null) {
                    musicInfo.getLast().setAlbum(content);
                }
            } else if (title.equals("Writers")) {
                if (musicInfo.size() != 0 && musicInfo.getLast().getSongwriter() == null) {
                    musicInfo.getLast().setSongwriter(content);
                }
            } else if (title.equals("Licensed to YouTube by")) {
                if (musicInfo.size() != 0 && musicInfo.getLast().getLicense() == null) {
                    musicInfo.getLast().setLicense(content);
                }
            }
        }
        return musicInfo.toArray(new MusicInfo[0]);
    }

    private String getText(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false)); //makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n");
        return Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    private long ytDurationToMillis(String rawDuration) {
        long duration = 0;
        Matcher secondMatcher = YT_DURATION_PATTERN_SECONDS.matcher(rawDuration);
        Matcher minuteMatcher = YT_DURATION_PATTERN_MINUTES.matcher(rawDuration);
        Matcher hourMatcher = YT_DURATION_PATTERN_HOURS.matcher(rawDuration);
        if (secondMatcher.find()) {
            duration += Long.valueOf(secondMatcher.group(1)) * 1000;
        }
        if (minuteMatcher.find()) {
            duration += Long.valueOf(minuteMatcher.group(1)) * 1000 * 60;
        }
        if (hourMatcher.find()) {
            duration += Long.valueOf(hourMatcher.group(1)) * 1000 * 60 * 60;
        }
        return duration;
    }

}
