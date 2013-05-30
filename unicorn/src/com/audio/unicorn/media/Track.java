package com.audio.unicorn.media;

public class Track {
    private long mAlbumId;
    private String mTitle;

    public Track(long albumId, String title) {
        mAlbumId = albumId;
        mTitle = title;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(long albumId) {
        mAlbumId = albumId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
