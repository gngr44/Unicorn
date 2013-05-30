package com.audio.unicorn.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.audio.unicorn.R;

public class CircularImageView extends ImageView {

    private Path mPath;

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setImageResource(R.drawable.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mPath == null) {
            mPath = new Path();
        }
        final int x = (getLeft() + getRight()) / 2;
        final int y = (getTop() + getBottom()) / 2;
        final int radius = getWidth() / 2;
        mPath.addCircle(x, y, radius, Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();
    }
}
