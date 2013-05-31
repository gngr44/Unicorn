package com.audio.unicorn.fragment;

import java.io.FileNotFoundException;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.audio.unicorn.R;
import com.audio.unicorn.engine.UnicornAudioEngine;
import com.audio.unicorn.engine.exception.InvalidMediaTypeException;
import com.audio.unicorn.media.AlbumArtProvider;
import com.audio.unicorn.media.Track;
import com.audio.unicorn.processor.GainProcessor;
import com.audio.unicorn.view.TrackControlView;
import com.audio.unicorn.view.TrackControlView.OnDropListener;
import com.audio.unicorn.view.TrackOptionsView;
import com.audio.unicorn.view.VerticalSeekBar;
import com.audio.unicorn.view.VerticalSeekBar.SeekBarListener;

public class TrackControlFragment extends Fragment implements OnDropListener, OnClickListener, SeekBarListener,
        OnLongClickListener {

    private static final int ROTATION_DURATION = 4000;
    private static final int MAX_ROTATION_DURATION = 5000;
    private static final int MODE_GAIN = 0;
    private static final int MODE_SAMPLE_RATE = 1;

    private TrackControlView mControlView;
    private VerticalSeekBar mSeekBar;
    private PopupWindow mPopupWindow;
    private ObjectAnimator mSlotAnimation;

    private UnicornAudioEngine mAudioEngine;
    private GainProcessor mGainProcessor;

    private int mMode = MODE_GAIN;
    private float mGainValue = 1.0f;
    private float mSamplingRateValue = 0.5f;

    public static TrackControlFragment newInstance() {
        return new TrackControlFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_control, container, false);
        mControlView = (TrackControlView) view;
        mSeekBar = mControlView.getSeekBar();
        mSeekBar.setProgress(100);

        mControlView.setOnDropListener(this);
        mControlView.getTrackSlotView().setOnClickListener(this);
        mControlView.getTrackSlotView().setOnLongClickListener(this);
        mSeekBar.setListener(this);
        return view;
    }

    public void setMode(int mode) {
        mMode = mode;
        if (mode == MODE_GAIN) {
            mSeekBar.setProgress((int) (mGainValue * mSeekBar.getMax()));
            mSeekBar.setColor(Color.CYAN);
        } else if (mode == MODE_SAMPLE_RATE) {
            mSeekBar.setProgress((int) (mSamplingRateValue * mSeekBar.getMax()));
            mSeekBar.setColor(Color.RED);
        }
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
                mSlotAnimation.setDuration(getRotationDuration(0.5f));
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
        if (v.getId() == R.id.TrackSlotView) {
            if (mAudioEngine != null) {
                if (mAudioEngine.isPlaying()) {
                    stopEngine();
                } else {
                    startEngine();
                }
            }
        } else if (v.getId() == R.id.GainOption) {
            setMode(MODE_GAIN);
            mPopupWindow.dismiss();
        } else if (v.getId() == R.id.SampleRateOption) {
            setMode(MODE_SAMPLE_RATE);
            mPopupWindow.dismiss();
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

    @Override
    public void onProgressChanged(VerticalSeekBar seekBar, int progress) {
        if (mAudioEngine == null) {
            return;
        }
        float percentage = (float) progress / seekBar.getMax();
        if (mMode == MODE_GAIN) {
            setGain(percentage);
        } else if (mMode == MODE_SAMPLE_RATE) {
            setSamplingRate(percentage);
        }
    }

    private void setGain(float percentage) {
        if (mGainProcessor == null) {
            mGainProcessor = new GainProcessor();
            mAudioEngine.addProcessor(mGainProcessor);
        }
        mGainValue = percentage;
        mGainProcessor.setGain(mGainValue);
    }

    private void setSamplingRate(float percentage) {
        mSamplingRateValue = percentage;
        mAudioEngine.setSamplingRate(mSamplingRateValue);
        if (mSlotAnimation != null) {
            long duration = getRotationDuration(percentage);
            mSlotAnimation.setDuration(duration);
        }
    }

    private static long getRotationDuration(float percentage) {
        return (long) (MAX_ROTATION_DURATION - (ROTATION_DURATION * percentage));
    }

    @Override
    public boolean onLongClick(View v) {
        float extraSize = 100 * getResources().getDisplayMetrics().density + 0.5f;
        if (mPopupWindow == null) {
            TrackOptionsView trackOptionsView = new TrackOptionsView(getActivity());
            trackOptionsView.getGainView().setOnClickListener(this);
            trackOptionsView.getSampleRateView().setOnClickListener(this);
            mPopupWindow = new PopupWindow(trackOptionsView);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
            mPopupWindow.setOutsideTouchable(true);
        }
        int[] location = new int[2];
        float rotation = v.getRotation();
        v.setRotation(0);
        v.getLocationInWindow(location);
        v.setRotation(rotation);
        mPopupWindow.setWidth((int) (v.getWidth() + extraSize));
        mPopupWindow.setHeight((int) (v.getHeight() + extraSize));
        location[0] -= extraSize / 2;
        location[1] -= extraSize / 2;

        Log.d("TEST", "show popup x: " + location[0] + "y: " + location[1]);
        mPopupWindow.showAtLocation(getView(), Gravity.NO_GRAVITY, (int) location[0], (int) location[1]);

        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleX", 0.5f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mPopupWindow.getContentView(), "scaleY", 0.5f, 1.0f);
        animationSet.playTogether(scaleX, scaleY);
        animationSet.start();
        return true;
    }
}
