package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import com.audio.unicorn.R;
import com.audio.unicorn.view.TrackControlView.OnDropListener;

public class MainView extends FrameLayout implements OnTouchListener {

    private TrackOptionsView mTrackOptionsView;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.main_view, this);
        setClipChildren(false);
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
