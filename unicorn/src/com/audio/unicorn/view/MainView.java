package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.audio.unicorn.R;

public class MainView extends FrameLayout {

    private CenterPlayingView mCenterView;
    private TrackView mTrackView1;
    private TrackView mTrackView2;
    private TrackView mTrackView3;
    private TrackView mTrackView4;
    private TrackView mTrackView5;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.main_view, this);
        mCenterView = (CenterPlayingView) findViewById(R.id.CenterView);
        mTrackView1 = (TrackView) findViewById(R.id.TrackView1);
        mTrackView2 = (TrackView) findViewById(R.id.TrackView2);
        mTrackView3 = (TrackView) findViewById(R.id.TrackView3);
        mTrackView4 = (TrackView) findViewById(R.id.TrackView4);
        mTrackView5 = (TrackView) findViewById(R.id.TrackView5);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int centerViewLeft = mCenterView.getLeft();
        final int centerViewTop = mCenterView.getTop();
        final int centerViewRight = mCenterView.getRight();
        final int centerViewBottom = mCenterView.getBottom();

        mTrackView1.layout(centerViewLeft, centerViewTop, centerViewLeft + mTrackView1.getMeasuredWidth(),
                centerViewTop + mTrackView1.getMeasuredHeight());

        mTrackView2.layout(centerViewRight - mTrackView2.getMeasuredWidth(), centerViewTop, centerViewRight,
                centerViewTop + mTrackView1.getMeasuredHeight());

        mTrackView3.layout(centerViewRight - mTrackView3.getMeasuredWidth(),
                centerViewBottom - mTrackView3.getMeasuredHeight(), centerViewRight, centerViewBottom);

        mTrackView4.layout(centerViewLeft, centerViewBottom - mTrackView4.getMeasuredHeight(), centerViewLeft
                + mTrackView1.getMeasuredWidth(), centerViewBottom);
    }

}
