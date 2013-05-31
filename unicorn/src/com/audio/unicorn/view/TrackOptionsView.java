package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.audio.unicorn.R;

public class TrackOptionsView extends FrameLayout {

    private TextView mGainView;
    private TextView mSampleRateView;

    public TrackOptionsView(Context context) {
        this(context, null);
    }

    public TrackOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public View getGainView() {
        return mGainView;
    }

    public View getSampleRateView() {
        return mSampleRateView;
    }

    private void init(Context context) {
        View.inflate(context, R.layout.track_options_view, this);
        mGainView = (TextView) findViewById(R.id.GainOption);
        mSampleRateView = (TextView) findViewById(R.id.SampleRateOption);
    }
}
