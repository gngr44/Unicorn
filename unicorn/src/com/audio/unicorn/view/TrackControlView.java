package com.audio.unicorn.view;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audio.unicorn.R;
import com.audio.unicorn.dragdrop.DragDropUtil;
import com.audio.unicorn.media.Track;

public class TrackControlView extends LinearLayout implements OnDragListener {

    public interface OnDropListener {
        public void onDrop(TrackControlView view, Track track);
    }

    private TrackView mTrackSlotView;
    private OnDropListener mListener;
    private VerticalSeekBar mSeekBar;
    private TextView mModeText;

    public TrackControlView(Context context) {
        this(context, null);
    }

    public TrackControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.track_control_view, this);
        mTrackSlotView = (TrackView) findViewById(R.id.TrackSlotView);
        mModeText = (TextView) findViewById(R.id.ControlText);
        mSeekBar = (VerticalSeekBar) findViewById(R.id.SeekBar);

        mTrackSlotView.setOnDragListener(this);
    }

    public void setOnDropListener(OnDropListener listener) {
        mListener = listener;
    }

    public TrackView getTrackSlotView() {
        return mTrackSlotView;
    }

    public TextView getModeTextView() {
        return mModeText;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENTERED:
                mTrackSlotView.animate().scaleX(1.3f).scaleY(1.3f);
                invalidate();
                break;
            case DragEvent.ACTION_DRAG_EXITED:
            case DragEvent.ACTION_DRAG_ENDED:
                resetAnimation();
                invalidate();
                break;
            case DragEvent.ACTION_DROP:
                ClipData clipData = event.getClipData();
                Item item = clipData.getItemAt(0);
                Intent intent = item.getIntent();
                long albumId = intent.getLongExtra(DragDropUtil.EXTRA_ALBUM_ID, 0);
                long trackId = intent.getLongExtra(DragDropUtil.EXTRA_TRACK_ID, 0);
                String title = intent.getStringExtra(DragDropUtil.EXTRA_TITLE);
                String filePath = intent.getStringExtra(DragDropUtil.EXTRA_FILE_PATH);

                if (mListener != null) {
                    mListener.onDrop(this, new Track(trackId, albumId, title, filePath));
                }
                break;
        }
        return true;
    }

    private void resetAnimation() {
        mTrackSlotView.animate().translationY(0).scaleX(1.0f).scaleY(1.0f);
    }

    public VerticalSeekBar getSeekBar() {
        return mSeekBar;
    }

}
