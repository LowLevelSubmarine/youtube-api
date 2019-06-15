package com.lowlevelsubmarine.youtube_api;

public class AGenDesc {

    private final String title;
    private final String artist;
    private final String album;

    public AGenDesc(String title, String artist, String album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

}
