package me.xeno.unengtrainer.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;
import android.widget.TextView;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.drawable.JumpDrawable;
import me.xeno.unengtrainer.drawable.SpinDrawable;

/**
 * Created by Administrator on 2018/7/20.
 */

public class LoadingDialog extends Dialog {

    private AppCompatImageView mImageView;
    private TextView mTextView;

    private SpinDrawable drawable;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mImageView = (AppCompatImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);

        drawable = new SpinDrawable(context.getResources().getDrawable(R.drawable.ic_football_loading));

        mImageView.setImageDrawable(drawable);
        drawable.start();
    }


}
