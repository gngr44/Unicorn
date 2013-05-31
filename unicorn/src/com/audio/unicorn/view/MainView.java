package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import com.audio.unicorn.R;

public class MainView extends FrameLayout implements OnTouchListener {

    private CenterPlayingView mCenterView;
    private TrackView mTrackView1;
    private TrackView mTrackView2;
    private TrackView mTrackView3;
    private TrackView mTrackView4;

    private TrackOptionsView mTrackOptionsView;

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
        mTrackOptionsView = (TrackOptionsView) findViewById(R.id.TrackOptionsView);

        mTrackView1.setOnTouchListener(this);
        mTrackView2.setOnTouchListener(this);
        mTrackView3.setOnTouchListener(this);
        mTrackView4.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTrackOptionsView.setAnchorView(v);
                mTrackOptionsView.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_UP:
                mTrackOptionsView.setVisibility(View.GONE);
                break;
        }
        return true;
    }
}
