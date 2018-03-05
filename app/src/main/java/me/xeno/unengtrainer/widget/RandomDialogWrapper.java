package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
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
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.activity.MainActivity;

/**
 * Created by xeno on 2017/7/28.
 */

public class RandomDialogWrapper implements View.OnClickListener {

//    private ArrayList<GoalkeeperModeTacticItemView> mTacticItemList = new ArrayList<>();
    private static final int TACTIC_LIMIT = 5;

    private static final int PLAY_MODE_FORWARD = 0;
    private static final int PLAY_MODE_RANDOM = 1;
    private int mPlayMode;
    private int mCurrentPlaying = -1;

    private MainActivity mActivity;

    private MaterialDialog mDialog;

//    private DialogInterface.OnDismissListener mOndismissListener;

//    private TextView mElevationAngleView;//显示当前仰角
//    private TextView mSwingAngleView;//显示

//    private int mLeftSpeed;
//    private int mRightSpeed;

//    private String mElevationAngle;
//    private String mSwingAngle;

    private TextView btn;

    private LinearLayout mTacticsLayout;
    private View mAddTacticView;

    private AppCompatRadioButton mForwardRadioBtn;
    private AppCompatRadioButton mRandomRadioBtn;

//    private Disposable mRandomTaskDisposable;

    public RandomDialogWrapper(Context context, String elevationAngle, String swingAngle) {
        mActivity = (MainActivity)context;
//        mLeftSpeed = leftSpeed;
//        mRightSpeed = rightSpeed;
//        mElevationAngle = elevationAngle;
//        mSwingAngle = swingAngle;
        init(context);
    }

    private void init(Context context) {
        mDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.view_random, false)
                .canceledOnTouchOutside(false)
                .title("随机模式")
                .build();

        btn =(TextView) mDialog.findViewById(R.id.confirm);

        mForwardRadioBtn = (AppCompatRadioButton) mDialog.findViewById(R.id.rb_forward);
        mRandomRadioBtn = (AppCompatRadioButton) mDialog.findViewById(R.id.rb_random);
//        mElevationAngleView = (TextView) mDialog.findViewById(R.id.elevation_angle);
//        mSwingAngleView = (TextView) mDialog.findViewById(R.id.swing_angle);

        mTacticsLayout = (LinearLayout) mDialog.findViewById(R.id.tactics_layout);
        mAddTacticView = mDialog.findViewById(R.id.add_tactic);

//        mElevationAngleView.setText(mElevationAngle);
//        mSwingAngleView.setText(mSwingAngle);

        btn.setOnClickListener(this);
        mAddTacticView.setOnClickListener(this);

        mForwardRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mPlayMode = PLAY_MODE_FORWARD;
            }
        });
        mRandomRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mPlayMode = PLAY_MODE_RANDOM;
            }
        });
        //默认顺序
        mForwardRadioBtn.setChecked(true);
        mPlayMode = PLAY_MODE_FORWARD;
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
                if(mTacticsLayout.getChildCount() <= 1)
                    return;

                    if(mPlayMode == PLAY_MODE_FORWARD) {
                        playNextForward();
                    } else if(mPlayMode == PLAY_MODE_RANDOM) {
                        playNextRandom();
                    }

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

            case R.id.add_tactic:
                GoalkeeperModeTacticItemView.OnRemoveListener listener = new GoalkeeperModeTacticItemView.OnRemoveListener() {
                    @Override
                    public void onRemove(View v, int index) {
                        Logger.warning("onRemove(): 当前共" + mTacticsLayout.getChildCount() +"个child，删除第" + index);
                        mTacticsLayout.removeViewAt(index);
                        //删除一项的时候，更新每一项的index
                        for(int i=0; i<mTacticsLayout.getChildCount()-1; i++){
                            GoalkeeperModeTacticItemView view = ((GoalkeeperModeTacticItemView)mTacticsLayout.getChildAt(i));
                            view.updateIndex(i);
                            view.setName((i+1)+".");
                        }
                        if(mAddTacticView.getVisibility() == View.GONE) {
                            mAddTacticView.setVisibility(View.VISIBLE);
                        }
                        //只要删除了就从头开始
                        mCurrentPlaying = -1;
                    }
                };
                Logger.warning("add: 当前共"+mTacticsLayout.getChildCount()+"个child");
                GoalkeeperModeTacticItemView itemView = new GoalkeeperModeTacticItemView(mActivity, mTacticsLayout.getChildCount() - 1, listener);
                mTacticsLayout.addView(itemView, mTacticsLayout.getChildCount() - 1);
                itemView.setName((mTacticsLayout.getChildCount()-1)+".");
                //超过个数隐藏添加按钮
                if(mTacticsLayout.getChildCount() > TACTIC_LIMIT) {
                    mAddTacticView.setVisibility(View.GONE);
                }
                break;
        }
    }


    private void playNextRandom() {
        //重复没有意义
        Random r = new Random();
        int lastPlay = mCurrentPlaying;
        do {
            mCurrentPlaying = r.nextInt(mTacticsLayout.getChildCount() - 1);
            Logger.warning(mCurrentPlaying);

            for (int i = 0; i < mTacticsLayout.getChildCount() - 1; i++) {
                if (i == mCurrentPlaying) {
                    GoalkeeperModeTacticItemView itemView = (GoalkeeperModeTacticItemView) mTacticsLayout.getChildAt(i);
                    itemView.setPlaying(true);
                    mActivity.getPresenter().setMotorSpeed(itemView.getLeftSpeed(), itemView.getRightSpeed());
                } else {
                    GoalkeeperModeTacticItemView itemView = (GoalkeeperModeTacticItemView) mTacticsLayout.getChildAt(i);
                    itemView.setPlaying(false);
                }
            }
        } while (lastPlay == mCurrentPlaying);
    }

    private void playNextForward() {
        mCurrentPlaying++;
        if(mCurrentPlaying > mTacticsLayout.getChildCount() - 2) {
            mCurrentPlaying = 0;
        }

        for(int i=0;i<mTacticsLayout.getChildCount()-1;i++){
            if(i == mCurrentPlaying) {
                GoalkeeperModeTacticItemView itemView = (GoalkeeperModeTacticItemView) mTacticsLayout.getChildAt(i);
                itemView.setPlaying(true);
                mActivity.getPresenter().setMotorSpeed(itemView.getLeftSpeed(), itemView.getRightSpeed());
            } else {
                GoalkeeperModeTacticItemView itemView = (GoalkeeperModeTacticItemView) mTacticsLayout.getChildAt(i);
                itemView.setPlaying(false);
            }
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
