package com.lowlevelsubmarine.youtube_api;

public class Video {

    private String id;
    private String title;
    private String description;
    private String channelId;
    private String channelName;
    private long duration;
    private MusicInfo[] musicInfos;
    private Category category;
    private AGenDesc aGenDesc;

    public String getId() {
        return this.id;
    }
    void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    void setDescription(String description) {
        this.description = description;
    }

    public String getChannelId() {
        return this.channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getDuration() {
        return duration;
    }
    void setDuration(long duration) {
        this.duration = duration;
    }

    public MusicInfo[] getMusicInfos() {
        return musicInfos;
    }
    void setMusicInfos(MusicInfo[] musicInfos) {
        this.musicInfos = musicInfos;
    }

    public Category getCategory() {
        return this.category;
    }
    void setCategory(Category category) {
        this.category = category;
    }

    public AGenDesc getAGenDesc() {
        return this.aGenDesc;
    }
    public void setAGenDesc(AGenDesc aGenDesc) {
        this.aGenDesc = aGenDesc;
    }

    //https://creatoracademy.youtube.com/page/lesson/overview-categories?cid=platform&hl=en#strategies-zippy-link-1
    public enum Category {

        COMEDY("Comedy"),
        EDUCATION("Education"),
        ENTERTAINMENT("Entertainment"),
        FAMILY_ENTERTAINMENT("Family Entertainment"),
        FOOD("Food"),
        GAMING("Gaming"),
        MUSIC("Music"),
        SPORTS("Sports");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

}
