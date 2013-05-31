package com.audio.unicorn.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalSeekBar extends View {

    private static final int MIN_ALPHA = 100;

    public interface SeekBarListener {
        public void onProgressChanged(VerticalSeekBar seekBar, int progress);
    }

    private int mProgress;
    private int mMax = 100;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();
    private SeekBarListener mListener;

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.CYAN);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setListener(SeekBarListener listener) {
        mListener = listener;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public void setProgress(int progress, boolean animate) {
        if (animate) {
            ObjectAnimator.ofInt(this, "progress", progress).start();
        } else {
            setProgress(progress);
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void setMax(int max) {
        mMax = max;
        invalidate();
    }

    public int getMax() {
        return mMax;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.left = getPaddingLeft();
        mRect.right = w - getPaddingRight();
        mRect.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int top = (int) (((float) (mMax - mProgress) / mMax) * getHeight());
        mRect.top = top;
        mPaint.setAlpha((int) (MIN_ALPHA + ((255 - MIN_ALPHA) * ((float) mProgress / mMax))));
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                final int top = (int) ((y / getHeight()) * mMax);
                int progress = mMax - top;
                progress = Math.max(0, progress);
                progress = Math.min(mMax, progress);
                setProgress(progress);
                if (mListener != null) {
                    mListener.onProgressChanged(this, progress);
                }
                break;
        }
        return true;
    }
}
