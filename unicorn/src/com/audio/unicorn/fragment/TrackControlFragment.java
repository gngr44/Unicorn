package com.audio.unicorn.fragment;

import com.audio.unicorn.R;
import com.audio.unicorn.engine.UnicornAudioEngine;
import com.audio.unicorn.engine.exception.InvalidMediaTypeException;
import com.audio.unicorn.media.Track;
import com.audio.unicorn.view.TrackControlView;
import com.audio.unicorn.view.TrackControlView.OnDropListener;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class TrackControlFragment extends Fragment implements OnDropListener, OnClickListener {

    private TrackControlView mControlView;
    private UnicornAudioEngine mAudioEngine;

    public static TrackControlFragment newInstance() {
        return new TrackControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_control, container, false);
        mControlView = (TrackControlView) view;
        mControlView.setOnDropListener(this);
        mControlView.getTrackSlotView().setOnClickListener(this);
        return view;
    }

    @Override
    public void onDrop(TrackControlView view, Track track) {
        Log.d("TEST", "drop: " + track.getTrackId() + " albumId: " + track.getAlbumId() + " title: " + track.getTitle());
        String filePath = track.getFilePath();
        if (mAudioEngine != null) {
            mAudioEngine.destroy();
        }
        mAudioEngine = new UnicornAudioEngine(filePath);
        startEngine();
    }

    private void startEngine() {
        try {
            mAudioEngine.start();
        } catch (InvalidMediaTypeException e) {
            Log.e("TEST", "", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (mAudioEngine != null) {
            if (mAudioEngine.isPlaying()) {
                mAudioEngine.stop();
            } else {
                startEngine();
            }

        }
    }
}
