package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.RxUtils;
import me.xeno.unengtrainer.util.TimeUtils;
import me.xeno.unengtrainer.view.activity.MainActivity;

/**
 * Created by xeno on 2017/7/28.
 */

public class RandomDialogWrapper implements View.OnClickListener {

    private MainActivity mActivity;

    private MaterialDialog mDialog;

//    private AppCompatSeekBar mLeftSpeedBar;
//    private AppCompatSeekBar mRightSpeedBar;
//
//    private View mLeftReduceView;
//    private View mLeftReduceDoubleView;
//    private View mLeftIncreaseView;
//    private View mLeftIncreaseDoubleView;
//
//    private View mRightReduceView;
//    private View mRightReduceDoubleView;
//    private View mRightIncreaseView;
//    private View mRightIncreaseDoubleView;
//
//    private TextView mLeftSpeedView;
//    private TextView mRightSpeedView;

//    private DialogInterface.OnDismissListener mOndismissListener;

    private TextView mElevationAngleView;//显示当前仰角
    private TextView mSwingAngleView;//显示

    private EditText mBorder1LeftSpeedEdt;
    private EditText mBorder1RightSpeedEdt;
    private EditText mBorder2LeftSpeedEdt;
    private EditText mBorder2RightSpeedEdt;

//    private int mLeftSpeed;
//    private int mRightSpeed;

    private String mElevationAngle;
    private String mSwingAngle;

    private TextView btn;
    private TextView currentView;

//    private Disposable mRandomTaskDisposable;

    public RandomDialogWrapper(Context context, String elevationAngle, String swingAngle) {
        mActivity = (MainActivity)context;
//        mLeftSpeed = leftSpeed;
//        mRightSpeed = rightSpeed;
        mElevationAngle = elevationAngle;
        mSwingAngle = swingAngle;
        init(context);
    }

    private void init(Context context) {
        mDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.view_random, false)
                .title("随机模式")
                .build();

        mBorder1LeftSpeedEdt = (EditText) mDialog.findViewById(R.id.border1_left_speed);
        mBorder1RightSpeedEdt = (EditText) mDialog.findViewById(R.id.border1_right_speed);
        mBorder2LeftSpeedEdt = (EditText) mDialog.findViewById(R.id.border2_left_speed);
        mBorder2RightSpeedEdt = (EditText) mDialog.findViewById(R.id.border2_right_speed);
        btn =(TextView) mDialog.findViewById(R.id.confirm);
        currentView = (TextView) mDialog.findViewById(R.id.current);

        mElevationAngleView = (TextView) mDialog.findViewById(R.id.elevation_angle);
        mSwingAngleView = (TextView) mDialog.findViewById(R.id.swing_angle);

//        mLeftSpeedBar = (AppCompatSeekBar) mDialog.findViewById(R.id.left_speed);
//        mRightSpeedBar = (AppCompatSeekBar) mDialog.findViewById(R.id.right_speed);
//
//        mLeftReduceView = mDialog.findViewById(R.id.left_reduce);
//        mLeftReduceDoubleView = mDialog.findViewById(R.id.left_reduce_double);
//        mLeftIncreaseView = mDialog.findViewById(R.id.left_increase);
//        mLeftIncreaseDoubleView = mDialog.findViewById(R.id.left_increase_double);
//        mRightReduceView = mDialog.findViewById(R.id.right_reduce);
//        mRightReduceDoubleView = mDialog.findViewById(R.id.right_reduce_double);
//        mRightIncreaseView = mDialog.findViewById(R.id.right_increase);
//        mRightIncreaseDoubleView = mDialog.findViewById(R.id.right_increase_double);
//
//        mLeftSpeedView = (TextView) mDialog.findViewById(R.id.left_speed_text);
//        mRightSpeedView = (TextView) mDialog.findViewById(R.id.right_speed_text);
//
//        mLeftReduceView.setOnClickListener(this);
//        mLeftReduceDoubleView.setOnClickListener(this);
//        mLeftIncreaseView.setOnClickListener(this);
//        mLeftIncreaseDoubleView.setOnClickListener(this);
//        mRightReduceView.setOnClickListener(this);
//        mRightReduceDoubleView.setOnClickListener(this);
//        mRightIncreaseView.setOnClickListener(this);
//        mRightIncreaseDoubleView.setOnClickListener(this);

//        mLeftSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mLeftSpeed = progress;
//                mLeftSpeedView.setText(progress + "%");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Logger.info("拖动结束，调用接口");
//                mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
//            }
//        });
//
//        mRightSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mRightSpeed = progress;
//                mRightSpeedView.setText(progress + "%");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Logger.info("拖动结束，调用接口");
//                mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
//            }
//        });

//        mLeftSpeedView.setText(mLeftSpeed + "%");
//        mRightSpeedView.setText(mRightSpeed + "%");
//        mLeftSpeedBar.setProgress(mLeftSpeed);
//        mRightSpeedBar.setProgress(mRightSpeed);

        mElevationAngleView.setText(mElevationAngle);
        mSwingAngleView.setText(mSwingAngle);

        btn.setOnClickListener(this);
    }



    public void showDialog() {
        mDialog.show();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }

//    public int getLeftSpeed() {
//        return mLeftSpeed;
//    }
//
//    public int getRightSpeed() {
//        return mRightSpeed;
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.confirm:

                nextSpeed(mBorder1LeftSpeedEdt.getText().toString(),
                            mBorder1RightSpeedEdt.getText().toString(),
                            mBorder2LeftSpeedEdt.getText().toString(),
                            mBorder2RightSpeedEdt.getText().toString());

//                if(mRandomTaskDisposable != null && !mRandomTaskDisposable.isDisposed()) {
//                    //停止任务
//                    mRandomTaskDisposable.dispose();
//                    //停止任务同时关闭电机
//                    mActivity.getPresenter().setMotorSpeed(0,0);
//                    btn.setText("开始");
//                } else {
//                    //启动任务，按钮变成「停止」
//                    startRandomTask(mBorder1LeftSpeedEdt.getText().toString(),
//                            mBorder1RightSpeedEdt.getText().toString(),
//                            mBorder2LeftSpeedEdt.getText().toString(),
//                            mBorder2RightSpeedEdt.getText().toString());
//                    btn.setText("停止");
//                }
                break;
//            case R.id.left_reduce_double:
//                if(mLeftSpeed > 4)
//                changeLeftSpeed(mLeftSpeed-5);
//                break;
//            case R.id.left_increase:
//                if(mLeftSpeed < 100)
//                changeLeftSpeed(mLeftSpeed+1);
//                break;
//            case R.id.left_increase_double:
//                if(mLeftSpeed < 96)
//                changeLeftSpeed(mLeftSpeed+5);
//                break;
//            case R.id.right_reduce:
//                if(mRightSpeed > 0)
//                    changeRightSpeed(mRightSpeed-1);
//                break;
//            case R.id.right_reduce_double:
//                if(mRightSpeed > 4)
//                    changeRightSpeed(mRightSpeed-5);
//                break;
//            case R.id.right_increase:
//                if(mRightSpeed < 100)
//                changeRightSpeed(mRightSpeed+1);
//                break;
//            case R.id.right_increase_double:
//                if(mRightSpeed < 96)
//                changeRightSpeed(mRightSpeed+5);
//                break;
        }
    }

    private void nextSpeed(String border1l,String border1r,String border2l,String border2r) {
        final int border1lInt = Integer.valueOf(border1l);
        final int border1rInt = Integer.valueOf(border1r);
        final int border2lInt = Integer.valueOf(border2l);
        final int border2rInt = Integer.valueOf(border2r);

        int border3lInt = 60;
        int border3rInt = 60;

        final Random r = new Random();

        //生成随机数
        int randomNum = r.nextInt(3);

        int leftSpeed = 0;
        int rightSpeed = 0;

        if(randomNum == 0) {
            leftSpeed = border1lInt;
            rightSpeed = border1rInt;
        } else if(randomNum == 1) {
            leftSpeed = border2lInt;
            rightSpeed = border2rInt;
        } else if(randomNum == 2) {
            leftSpeed = border3lInt;
            rightSpeed = border3rInt;
        }

        currentView.setText("当前转速：" + leftSpeed + ", " + rightSpeed);

//                        if(Config.isDebugging())
        Logger.warning("random data sent: left: " + leftSpeed + ", right: " + rightSpeed);
        mActivity.getPresenter().setMotorSpeed(leftSpeed, rightSpeed);
    }

    private void fgweag() {

    }


    private void startRandomTask(String border1l,String border1r,String border2l,String border2r) {
        //每x秒以当前边界为限随机一组左右轮速数据执行


        final int border1lInt = Integer.valueOf(border1l);
        final int border1rInt = Integer.valueOf(border1r);
        final int border2lInt = Integer.valueOf(border2l);
        final int border2rInt = Integer.valueOf(border2r);

        Logger.warning("border1lInt:" + border1lInt);
        Logger.warning("border1rInt:" + border1rInt);

        final Random r = new Random();
        final Random r2 = new Random();

        //每隔x秒改变一次
        Observable.interval(0, 4, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {

                    }
                });
    }

//    private void changeLeftSpeed(int newSpeed) {
//        mLeftSpeed = newSpeed;
//        mLeftSpeedView.setText(mLeftSpeed + "%");
//        mLeftSpeedBar.setProgress(mLeftSpeed);
//        mActivity.getPresenter().setMotorSpeed(mLeftSpeed, mRightSpeed);
//    }
//
//    private void changeRightSpeed(int newSpeed) {
//        mRightSpeed = newSpeed;
//        mRightSpeedView.setText(mRightSpeed + "%");
//        mRightSpeedBar.setProgress(mRightSpeed);
//        mActivity.getPresenter().setMotorSpeed(mRightSpeed, mRightSpeed);
//    }
}
