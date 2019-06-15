package com.lowlevelsubmarine.youtube_api;

public class SearchResult {

    private String id;
    private String title;
    private String descriptionPreview;
    private String user;
    private long duration;
    private boolean aGen;

    public String getId() {
        return this.id;
    }
    void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }
    void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return this.user;
    }
    void setUser(String user) {
        this.user = user;
    }

    public long getDuration() {
        return this.duration;
    }
    void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isAGen() {
        return this.aGen;
    }
    public void setAGen(boolean aGen) {
        this.aGen = aGen;
    }

    public String getDescriptionPreview() {
        return this.descriptionPreview;
    }
    public void setDescriptionPreview(String descriptionPreview) {
        this.descriptionPreview = descriptionPreview;
    }

}
