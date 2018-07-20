package me.xeno.unengtrainer.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/7/20.
 */

public class JumpDrawable extends Drawable implements Animatable {

    private float scaleX = 1;
    private float scaleY = 1;

    public Drawable drawable;

    private Rect bounds = getBounds();

    public JumpDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    private boolean isRunning;

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.scale(scaleX, scaleY);
        Log.i("xeno", "draw调用" + drawable);
        drawable.draw(canvas);
        changeScaleY();

        invalidateSelf();
    }

    private void changeScaleY() {
        if (scaleY >= 0)
            scaleY -= 0.005;
        else
            scaleY = 1;
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
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        drawable.setBounds(bounds);
    }
}
