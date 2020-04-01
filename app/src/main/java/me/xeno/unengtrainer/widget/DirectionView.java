package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.xeno.unengtrainer.R;

public class DirectionView extends View {

    private Paint mPaint;
    private Drawable mLeftDir;
    private Drawable mRightDir;
    private Drawable mUpDir;
    private Drawable mDownDir;

    public DirectionView(Context context) {
        super(context);
        init();
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xff000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(230);

        mLeftDir = getResources().getDrawable(R.drawable.ic_dir_left);
        mRightDir = getResources().getDrawable(R.drawable.ic_dir_right);
        mUpDir = getResources().getDrawable(R.drawable.ic_up);
        mDownDir = getResources().getDrawable(R.drawable.ic_down);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 3, mPaint);

        //左箭头的绘制中心
        int c = getWidth() / 2 - getWidth() / 3;
        mLeftDir.setBounds(c - 40, getHeight() / 2 - 40, c + 40, getHeight() / 2 + 40);
        mLeftDir.draw(canvas);
        int c2 = getWidth() / 2 + getWidth() / 3;
        mRightDir.setBounds(c2 - 40, getHeight() / 2 - 40, c2 + 40, getHeight() / 2 + 40);
        mRightDir.draw(canvas);

        mUpDir.setBounds(getWidth() / 2 - 50, c - 50, getWidth() / 2 + 50, c + 50);
        mUpDir.draw(canvas);
        mDownDir.setBounds(getWidth() / 2 - 50, c2 - 50, getWidth() / 2 +50, c2 + 50);
        mDownDir.draw(canvas);
    }
}
