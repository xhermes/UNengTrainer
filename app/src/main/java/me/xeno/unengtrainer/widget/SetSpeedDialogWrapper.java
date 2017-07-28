package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.activity.MainActivity;

/**
 * Created by xeno on 2017/7/28.
 */

public class SetSpeedDialogWrapper {

    private MainActivity mActivity;

    private MaterialDialog mDialog;

    private AppCompatSeekBar mLeftSpeedBar;
    private AppCompatSeekBar mRightSpeedBar;

    private TextView mLeftSpeedView;
    private TextView mRightSpeedView;

//    private DialogInterface.OnDismissListener mOndismissListener;

    private int mLeftSpeed;
    private int mRightSpeed;

    public SetSpeedDialogWrapper(Context context, int leftSpeed, int rightSpeed) {
        mActivity = (MainActivity)context;
        mLeftSpeed = leftSpeed;
        mRightSpeed = rightSpeed;
        init(context);
    }

    private void init(Context context) {
        mDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.view_set_speed, false)
                .title("设置转速")
                .build();
        mLeftSpeedBar = (AppCompatSeekBar) mDialog.findViewById(R.id.left_speed);
        mRightSpeedBar = (AppCompatSeekBar) mDialog.findViewById(R.id.right_speed);

        mLeftSpeedView = (TextView) mDialog.findViewById(R.id.left_speed_text);
        mRightSpeedView = (TextView) mDialog.findViewById(R.id.right_speed_text);

        mLeftSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLeftSpeed = progress;
                mLeftSpeedView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Logger.info("拖动结束，调用接口");
                mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
            }
        });

        mRightSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRightSpeed = progress;
                mRightSpeedView.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Logger.info("拖动结束，调用接口");
                mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
            }
        });

        mLeftSpeedView.setText(mLeftSpeed + "%");
        mRightSpeedView.setText(mRightSpeed + "%");
        mLeftSpeedBar.setProgress(mLeftSpeed);
        mRightSpeedBar.setProgress(mRightSpeed);
    }

    public void showDialog() {
        mDialog.show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }

    public int getLeftSpeed() {
        return mLeftSpeed;
    }

    public int getRightSpeed() {
        return mRightSpeed;
    }
}
