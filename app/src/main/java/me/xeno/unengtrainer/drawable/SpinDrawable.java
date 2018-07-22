package me.xeno.unengtrainer.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/7/21.
 */

public class SpinDrawable extends Drawable implements Animatable {

    private Drawable mDrawable;

    private float mCurrentDegree;

    public SpinDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.rotate(mCurrentDegree, getBounds().centerX(), getBounds().centerY());

        mDrawable.draw(canvas);
        invalidateSelf();
        mCurrentDegree+=1;
        if(mCurrentDegree >= 359){
            mCurrentDegree = 0;
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mDrawable.setBounds(bounds);
    }
}
