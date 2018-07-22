package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.activity.MainActivity;

/**
 * Created by xeno on 2017/7/28.
 */

public class SetSpeedDialogWrapper implements View.OnClickListener {

    private MainActivity mActivity;

    private MaterialDialog mDialog;

    private AppCompatSeekBar mLeftSpeedBar;
    private AppCompatSeekBar mRightSpeedBar;

    private View mLeftReduceView;
    private View mLeftReduceDoubleView;
    private View mLeftIncreaseView;
    private View mLeftIncreaseDoubleView;

    private View mRightReduceView;
    private View mRightReduceDoubleView;
    private View mRightIncreaseView;
    private View mRightIncreaseDoubleView;

    private TextView mLeftSpeedView;
    private TextView mRightSpeedView;

//    private DialogInterface.OnDismissListener mOndismissListener;

    private float mLeftSpeed;
    private float mRightSpeed;

    public SetSpeedDialogWrapper(Context context, float leftSpeed, float rightSpeed) {
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

        mLeftReduceView = mDialog.findViewById(R.id.left_reduce);
        mLeftReduceDoubleView = mDialog.findViewById(R.id.left_reduce_double);
        mLeftIncreaseView = mDialog.findViewById(R.id.left_increase);
        mLeftIncreaseDoubleView = mDialog.findViewById(R.id.left_increase_double);
        mRightReduceView = mDialog.findViewById(R.id.right_reduce);
        mRightReduceDoubleView = mDialog.findViewById(R.id.right_reduce_double);
        mRightIncreaseView = mDialog.findViewById(R.id.right_increase);
        mRightIncreaseDoubleView = mDialog.findViewById(R.id.right_increase_double);

        mLeftSpeedView = (TextView) mDialog.findViewById(R.id.left_speed_text);
        mRightSpeedView = (TextView) mDialog.findViewById(R.id.right_speed_text);

        mLeftReduceView.setOnClickListener(this);
        mLeftReduceDoubleView.setOnClickListener(this);
        mLeftIncreaseView.setOnClickListener(this);
        mLeftIncreaseDoubleView.setOnClickListener(this);
        mRightReduceView.setOnClickListener(this);
        mRightReduceDoubleView.setOnClickListener(this);
        mRightIncreaseView.setOnClickListener(this);
        mRightIncreaseDoubleView.setOnClickListener(this);

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
        mLeftSpeedBar.setProgress((int)mLeftSpeed);
        mRightSpeedBar.setProgress((int)mRightSpeed);
    }



    public void showDialog() {
        mDialog.show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }

    public float getLeftSpeed() {
        return mLeftSpeed;
    }

    public float getRightSpeed() {
        return mRightSpeed;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_reduce:
                if(mLeftSpeed > 0)
               changeLeftSpeed(mLeftSpeed-0.5f);
                break;
            case R.id.left_reduce_double:
                if(mLeftSpeed > 4)
                changeLeftSpeed(mLeftSpeed-2);
                break;
            case R.id.left_increase:
                if(mLeftSpeed < 100)
                changeLeftSpeed(mLeftSpeed+0.5f);
                break;
            case R.id.left_increase_double:
                if(mLeftSpeed < 96)
                changeLeftSpeed(mLeftSpeed+2);
                break;
            case R.id.right_reduce:
                if(mRightSpeed > 0)
                    changeRightSpeed(mRightSpeed-0.5f);
                break;
            case R.id.right_reduce_double:
                if(mRightSpeed > 4)
                    changeRightSpeed(mRightSpeed-2);
                break;
            case R.id.right_increase:
                if(mRightSpeed < 100)
                changeRightSpeed(mRightSpeed+0.5f);
                break;
            case R.id.right_increase_double:
                if(mRightSpeed < 96)
                changeRightSpeed(mRightSpeed+2);
                break;
        }
    }

    private void changeLeftSpeed(float newSpeed) {
        mLeftSpeed = newSpeed;
        mLeftSpeedView.setText(mLeftSpeed + "%");
        int leftSpeedInt = (int)mLeftSpeed;
        mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
        mLeftSpeedBar.setProgress(leftSpeedInt);
    }

    private void changeRightSpeed(float newSpeed) {
        mRightSpeed = newSpeed;
        mRightSpeedView.setText(mRightSpeed + "%");
        mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
        mRightSpeedBar.setProgress((int)mRightSpeed);
    }


}
