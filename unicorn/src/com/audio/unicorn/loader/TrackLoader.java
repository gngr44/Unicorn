package com.audio.unicorn.loader;

import java.util.List;

import android.content.Context;

import com.audio.unicorn.media.Track;
import com.audio.unicorn.media.TracksProvider;

public class TrackLoader extends BaseLoader<List<Track>> {

    public TrackLoader(Context context) {
        super(context);
    }

    @Override
    public List<Track> loadInBackground() {
        return new TracksProvider().queryTracks(getContext());
    }

}
