package com.audio.unicorn.fragment;

import java.io.FileNotFoundException;

import com.audio.unicorn.R;
import com.audio.unicorn.engine.UnicornAudioEngine;
import com.audio.unicorn.engine.exception.InvalidMediaTypeException;
import com.audio.unicorn.media.AlbumArtProvider;
import com.audio.unicorn.media.Track;
import com.audio.unicorn.view.TrackControlView;
import com.audio.unicorn.view.TrackControlView.OnDropListener;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class TrackControlFragment extends Fragment implements OnDropListener, OnClickListener {

    private TrackControlView mControlView;
    private UnicornAudioEngine mAudioEngine;
    private ObjectAnimator mSlotAnimation;

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
        new ImageLoadThread(getActivity(), mControlView.getTrackSlotView(), track.getAlbumId()).start();
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
            if (mSlotAnimation == null) {
                mSlotAnimation = ObjectAnimator.ofFloat(mControlView.getTrackSlotView(), "rotation", 360);
                mSlotAnimation.setDuration(2000);
                mSlotAnimation.setInterpolator(new LinearInterpolator());
                mSlotAnimation.setRepeatCount(ObjectAnimator.INFINITE);
            }
            mSlotAnimation.start();
        } catch (InvalidMediaTypeException e) {
            Log.e("TEST", "", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (mAudioEngine != null) {
            if (mAudioEngine.isPlaying()) {
                stopEngine();
            } else {
                startEngine();
            }

        }
    }

    private void stopEngine() {
        mAudioEngine.stop();
        mSlotAnimation.cancel();
    }

    private class ImageLoadThread extends Thread {

        private long mAlbumId;
        private FileNotFoundException mException;
        private Context mContext;
        private Handler mHandler;
        private ImageView mImageView;

        public ImageLoadThread(Context context, ImageView imageView, long albumId) {
            mContext = context;
            mImageView = imageView;
            mAlbumId = albumId;
            mHandler = new Handler();
        }

        @Override
        public void run() {
            try {
                final Bitmap bitmap = new AlbumArtProvider().getCircularAlbumArtwork(mContext, mAlbumId);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            } catch (FileNotFoundException e) {
                mException = e;
            }
        }

    }
}
