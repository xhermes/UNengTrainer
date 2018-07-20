package me.xeno.unengtrainer.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.drawable.JumpDrawable;

/**
 * Created by Administrator on 2018/7/20.
 */

public class LoadingDialog extends Dialog {

    private ImageView mImageView;
    private TextView mTextView;

    private JumpDrawable drawable;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);

        mImageView = (ImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);

        drawable = new JumpDrawable(context.getResources().getDrawable(R.drawable.ic_football));

        mImageView.setImageDrawable(drawable);
        drawable.start();
    }


}
