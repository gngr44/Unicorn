package com.audio.unicorn.media;

public class Track {
    private long mAlbumId;
    private String mTitle;
    private long mTrackId;
    private String mFilePath;

    public Track(long trackId, long albumId, String title, String filePath) {
        mTrackId = trackId;
        mAlbumId = albumId;
        mTitle = title;
        mFilePath = filePath;
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

    public long getTrackId() {
        return mTrackId;
    }

    public void setTrackId(long trackId) {
        mTrackId = trackId;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

}
