package com.audio.unicorn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.audio.unicorn.R;

public class TrackView extends FrameLayout {

    private TextView mTitleText;
    private ImageView mImageView;

    public TrackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.track_gallery_item_view, this);
        mTitleText = (TextView) findViewById(R.id.TitleText);
        mImageView = (ImageView) findViewById(R.id.ImageView);
    }

    public TextView getTitleText() {
        return mTitleText;
    }

    public ImageView getImageView() {
        return mImageView;
    }

}
