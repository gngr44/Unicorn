package com.audio.unicorn.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalSeekBar extends View {

    private int mProgress;
    private int mMax = 100;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.CYAN);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
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
        mRect.left = 0;
        mRect.right = w;
        mRect.bottom = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int top = (int) (((float) mProgress / mMax) * getHeight());
        mRect.top = top;
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                final int progress = (int) ((y / getHeight()) * mMax);
                setProgress(progress);
                break;
        }
        return true;
    }
}
