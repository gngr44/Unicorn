package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.audio.unicorn.R;

public class TrackOptionsView extends FrameLayout {

    private View mAnchorView;
    private ImageView mVolumeView;

    private int mAnchorLeft;
    private int mAnchorTop;
    private int mAnchorRight;
    private int mAnchorBottom;

    public TrackOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.track_options_view, this);
        mVolumeView = (ImageView) findViewById(R.id.VolumeOption);

        final View anchorView = findViewById(R.id.DefaultAnchorView);
        setAnchorView(anchorView);
        if (isInEditMode()) {
            mAnchorView.setVisibility(View.VISIBLE);
        }
    }

    public void setAnchorView(View view) {
        mAnchorView = view;
        mAnchorLeft = mAnchorView.getLeft();
        mAnchorTop = mAnchorView.getTop();
        mAnchorRight = mAnchorView.getRight();
        mAnchorBottom = mAnchorView.getBottom();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int anchorWidth = mAnchorRight - mAnchorLeft;
        final int anchorHeight = mAnchorBottom - mAnchorTop;
        final int anchorCenterX = mAnchorLeft + (anchorWidth / 2);
        final int anchorCenterY = mAnchorTop + (anchorHeight / 2);

        mVolumeView.layout(anchorCenterX - (mVolumeView.getMeasuredWidth() / 2),
                mAnchorTop - mVolumeView.getMeasuredHeight(), anchorCenterX + (mVolumeView.getMeasuredWidth() / 2),
                mAnchorTop);
    }
}
