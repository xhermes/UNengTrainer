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
import android.util.Log;

/**
 * Created by Administrator on 2018/7/20.
 * stage 1:加速向下
 * stage 2:缩小形变
 * stage 3:恢复形变
 * stage 4:减速向上
 */

public class JumpDrawable extends Drawable implements Animatable {

    public static final int STAGE_DROP = 1;
    public static final int STAGE_SCALE_DOWN = 2;
    public static final int STAGE_SCALE_RECOVER = 3;
    public static final int STAGE_RISE = 4;
    public static final int STAGE_STOP = 0;

    private static final float MAX_TRANSLATE = 50;

    private static final float MIN_SCALE = 0.8f;

    private static final float TRANSLATE_STEP = 0.5f;
    private static final float SCALE_STEP = 0.005f;

    private boolean mLoop = true;

    private float scaleX = 1;
    private float scaleY = 1;

    private Rect bounds = new Rect();

    private float translateY = 0;

    public Drawable drawable;

    private int mCurrentStage;

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
        switch (mCurrentStage) {
            case STAGE_DROP:
                doDrop();
                canvas.translate(0, translateY);
                break;
            case STAGE_SCALE_DOWN:
                doScaleDown();
                canvas.scale(scaleX, scaleY, bounds.centerX(), bounds.bottom);
                canvas.translate(0, translateY);
                break;
            case STAGE_SCALE_RECOVER:
                doRecover();
                canvas.scale(scaleX, scaleY, bounds.centerX(), bounds.bottom);
                canvas.translate(0, translateY);
                break;
            case STAGE_RISE:
                doRise();
                canvas.translate(0, translateY);
                break;
            case STAGE_STOP:
                if(mLoop) {
                    mCurrentStage = STAGE_DROP;
                    translateY =0;
                    scaleY=1;
                }
        }

//        Log.i("xeno", "draw调用" + drawable);
        drawable.draw(canvas);
        invalidateSelf();
    }


    private void doDrop() {
        if(translateY >= MAX_TRANSLATE) {
            mCurrentStage = STAGE_SCALE_DOWN;
        }else {
            translateY+=TRANSLATE_STEP;
        }
    }

    private void doScaleDown() {
        if (scaleY <= MIN_SCALE)
            mCurrentStage = STAGE_SCALE_RECOVER;
        else
            scaleY-=SCALE_STEP;
    }

    private void doRecover() {
        if(scaleY >= 1)
            mCurrentStage = STAGE_RISE;
        else
            scaleY+=SCALE_STEP;
    }

    private void doRise() {
        if(translateY >= 0)
            translateY-=TRANSLATE_STEP;
        else {
            mCurrentStage = STAGE_STOP;
            translateY =0;
            scaleY=1;
        }

    }

    private void setDrawableBounds() {
        bounds.top = getBounds().top /2;
        bounds.bottom = getBounds().bottom/2;
        bounds.left = getBounds().left/2;
        bounds.right = getBounds().right/2;
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
        setDrawableBounds();
        drawable.setBounds(this.bounds);
    }
}
