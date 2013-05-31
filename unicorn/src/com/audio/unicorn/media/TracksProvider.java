package com.audio.unicorn.media;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

public class TracksProvider {
    public List<Track> queryTracks(Context context) {
        final String[] projection = { Media.ALBUM_ID, Media.TITLE };
        final Cursor c = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        final List<Track> tracks = new ArrayList<Track>(c.getCount());
        final int albumIdIndex = c.getColumnIndex(Media.ALBUM_ID);
        final int titleIndex = c.getColumnIndex(Media.TITLE);
        while (c.moveToNext()) {
            final long albumId = c.getLong(albumIdIndex);
            final String trackTitle = c.getString(titleIndex);
            tracks.add(new Track(albumId, trackTitle));
        }
        c.close();
        return tracks;
    }
}
