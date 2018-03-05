package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Created by xeno on 2018/3/5.
 */

public class GoalkeeperModeTacticItemView extends LinearLayout {

    private int mIndex;

    private AppCompatImageView mPlayingView;
    private TextView mNameView;
    private AppCompatEditText mLeftEdt;
    private AppCompatEditText mRightEdt;
    private View mRemoveView;


    public GoalkeeperModeTacticItemView(Context context, int index, final OnRemoveListener listener) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_goalkeeper_mode_item, this);

        mPlayingView = (AppCompatImageView) findViewById(R.id.iv_playing);
        mNameView = (TextView) findViewById(R.id.name);
        mLeftEdt = (AppCompatEditText) findViewById(R.id.edt_left);
        mRightEdt = (AppCompatEditText) findViewById(R.id.edt_right);
        mRemoveView = findViewById(R.id.remove);

        mIndex = index;
        setPlaying(false);

        mRemoveView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemove(v, mIndex);
            }
        });
    }

    public int getLeftSpeed() {
        String spdStr = mLeftEdt.getText().toString();
        try {
            return Integer.valueOf(spdStr);
        } catch (Exception e) {
            return 0;
        }
    }
    public int getRightSpeed() {
        String spdStr = mRightEdt.getText().toString();
        try {
            return Integer.valueOf(spdStr);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setPlaying(boolean isPlaying) {
        if(isPlaying)
            mPlayingView.setVisibility(VISIBLE);
        else
            mPlayingView.setVisibility(INVISIBLE);
    }

    public void updateIndex(int index) {
        mIndex = index;
    }

    public void setName(String name) {
        mNameView.setText(name);
    }

    interface OnRemoveListener {
        void onRemove(View v, int index);
    }

}
