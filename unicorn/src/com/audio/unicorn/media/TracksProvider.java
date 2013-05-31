package com.audio.unicorn.media;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class TracksProvider {
    public List<Track> queryTracks(Context context) {
        final String[] projection = { Media._ID, Media.ALBUM_ID, Media.TITLE, Media.DATA };
        String selection = Media.IS_MUSIC + "=1";
        final Cursor c = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        final List<Track> tracks = new ArrayList<Track>(c.getCount());
        final int dataIndex = c.getColumnIndex(Media.DATA);
        final int trackIndex = c.getColumnIndex(Media._ID);
        final int albumIdIndex = c.getColumnIndex(Media.ALBUM_ID);
        final int titleIndex = c.getColumnIndex(Media.TITLE);
        while (c.moveToNext()) {
            final String filePath = c.getString(dataIndex);
            final long trackId = c.getLong(trackIndex);
            final long albumId = c.getLong(albumIdIndex);
            final String trackTitle = c.getString(titleIndex);
            tracks.add(new Track(trackId, albumId, trackTitle, filePath));
        }
        c.close();
        return tracks;
    }
}
