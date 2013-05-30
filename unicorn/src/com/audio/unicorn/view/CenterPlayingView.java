package com.audio.unicorn.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CenterPlayingView extends View {

    private Paint mPaint;

    public CenterPlayingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        final int actualWidth = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(actualWidth - 100, actualWidth - 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.CYAN);
        }
        final int padding = (int) (20 * getResources().getDisplayMetrics().density + 0.5f);
        final int radius = getWidth() / 2 - padding;
        canvas.drawCircle(getLeft() + getWidth() / 2, getTop() + getHeight() / 2, radius, mPaint);
    }
}
