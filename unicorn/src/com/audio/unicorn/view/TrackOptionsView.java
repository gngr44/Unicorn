package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.audio.unicorn.R;

public class TrackOptionsView extends FrameLayout {

    private ImageView mGainView;
    private ImageView mSampleRateView;

    public TrackOptionsView(Context context) {
        this(context, null);
    }

    public TrackOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageView getGainView() {
        return mGainView;
    }

    public ImageView getSampleRateView() {
        return mSampleRateView;
    }

    private void init(Context context) {
        View.inflate(context, R.layout.track_options_view, this);
        mGainView = (ImageView) findViewById(R.id.GainOption);
        mSampleRateView = (ImageView) findViewById(R.id.SampleRateOption);
    }
}
