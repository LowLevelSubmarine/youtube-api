package com.lowlevelsubmarine.youtube_api;

public class MusicInfo {

    private String title;
    private String artist;
    private String album;
    private String songwriter;
    private String license;

    public String getTitle() {
        return title;
    }
    void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }
    void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }
    void setAlbum(String album) {
        this.album = album;
    }

    public String getSongwriter() {
        return songwriter;
    }
    void setSongwriter(String songwriter) {
        this.songwriter = songwriter;
    }

    public String getLicense() {
        return license;
    }
    void setLicense(String license) {
        this.license = license;
    }

}
