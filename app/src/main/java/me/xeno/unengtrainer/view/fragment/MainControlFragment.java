package me.xeno.unengtrainer.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.util.DialogUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.RxUtils;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.activity.BaseActivity;
import me.xeno.unengtrainer.view.activity.FavouriteActivity;
import me.xeno.unengtrainer.view.activity.MainActivity;
import me.xeno.unengtrainer.widget.SetSpeedDialogWrapper;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class MainControlFragment extends BaseMainFragment implements View.OnTouchListener {

    private View mRunAxisSwingPositiveView;
    private View mRunAxisSwingNegativeView;
    private View mRunAxisElevationPositiveView;
    private View mRunAxisElevationNegativeView;

    private View mStopElectricView;
    private View mReturnToZeroView;
    private View mReturnToZero2View;

    private View mFromFavView;

    private View mSetMotorView;//设置左右电机速度
    private View mSetAngleView;//输入设置调整角度

    private TextView mCurrentSwingAngleView;
    private TextView mCurrentElevationAngleView;

    private View mSendView;

    private double mSwingAngle;
    private double mElevationAngle;
    private int mLeftSpeed;
    private int mRightSpeed;

    private double mCurrentSwingAngle;
    private double mCurrentElevationAngle;
    private int mCurrentLeftSpeed;
    private int mCurrentRightSpeed;

    private TextView mCurrentRightSpeedView;
    private TextView mCurrentLeftSpeedView;

    private int pick;

    private Disposable mBatteryDisposable;
    private Disposable mCurrentAngleDisposable;

    private Disposable mRunAxisSwingPosDisposable;
    private Disposable mRunAxisSwingNegDisposable;
    private Disposable mRunAxisElevationPosDisposable;
    private Disposable mRunAxisElevationNegDisposable;

    private Disposable mStatusDisposable;

    private final CompositeDisposable cd = new CompositeDisposable();

    public static MainControlFragment newInstance() {
        return new MainControlFragment();
    }

    public MainControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //页面恢复时，重新开始获取电压
        if (mBatteryDisposable == null)
            mBatteryDisposable = getMainActivity().getPresenter().getBatteryVoltage(Config.GET_BATTERY_PERIOD);
        if (mStatusDisposable == null) {
            mStatusDisposable = getMainActivity().getPresenter().startGetStatusTask(Config.GET_STATUS_PERIOD);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //页面休眠时，停止获取电压
        dispose(mBatteryDisposable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control_main, container, false);

        mSetMotorView =  root.findViewById(R.id.set_motor_speed);
        mSetAngleView =  root.findViewById(R.id.set_angle);

        mStopElectricView = root.findViewById(R.id.stop_both_electric);
        mReturnToZeroView = root.findViewById(R.id.return_to_zero);
        mReturnToZero2View = root.findViewById(R.id.return_to_zero2);

        mFromFavView = root.findViewById(R.id.from_fav);
//        mCurrentElevationAngleView = (TextView) root.findViewById(R.id.current_elevation_angle);
//        mCurrentSwingAngleView = (TextView) root.findViewById(R.id.current_swing_angle);
//
//
        mRunAxisSwingNegativeView = root.findViewById(R.id.swing_angle_negative);
        mRunAxisSwingPositiveView = root.findViewById(R.id.swing_angle_positive);
//        mRunAxisSwingStopView = (AppCompatImageView) root.findViewById(R.id.swing_angle_stop);
        mRunAxisElevationNegativeView = root.findViewById(R.id.elevation_angle_negative);
        mRunAxisElevationPositiveView = root.findViewById(R.id.elevation_angle_positive);
////        mRunAxisElevationStopView = (AppCompatImageView) root.findViewById(R.id.elevation_angle_stop);
//
//
//        mElevationAngleView = (TextView) root.findViewById(R.id.elevation_angle);
//        mSwingAngleView = (TextView) root.findViewById(R.id.swing_angle);
//        mLeftSpeedView = (TextView) root.findViewById(R.id.left_speed);
//        mRightSpeedView = (TextView) root.findViewById(R.id.right_speed);
//
//        mCurrentRightSpeedView = (TextView) root.findViewById(R.id.current_right_speed);
//        mCurrentLeftSpeedView = (TextView) root.findViewById(R.id.current_left_speed);
//
//
        initView();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    private void initView() {

//        mSendView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
//
//                getMainActivity().getPresenter().send(mSwingAngle, mElevationAngle, mLeftSpeed, mRightSpeed);
//            }
//        });

        mStopElectricView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentLeftSpeed = 0;
                mCurrentRightSpeed = 0;
                getMainActivity().getPresenter().setMotorSpeed(0, 0);
                getMainActivity().refreshCurrentSpeed(0, 0);
            }
        });
        mReturnToZeroView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().setAxisAngle(0, 0);
                startGetCurrentAngleTask();
            }
        });

        mRunAxisSwingPositiveView.setOnTouchListener(this);
        mRunAxisSwingNegativeView.setOnTouchListener(this);
        mRunAxisElevationPositiveView.setOnTouchListener(this);
        mRunAxisElevationNegativeView.setOnTouchListener(this);

        mSetMotorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetMotorSpeedDialog();
            }
        });

        mFromFavView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteActivity.goFromActivityForResult(new WeakReference<BaseActivity>(getMainActivity()));
            }
        });

//        mRunAxisSwingStopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().stopAxis();
//            }
//    });


//        mRunAxisElevationPositiveView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mRunAxisDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_PERIOD);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    dispose(mRunAxisDisposable);
//                    Logger.info("单轴运行结束");
//                }
//                return true;
//            }
//        });
//        mRunAxisElevationNegativeView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mRunAxisDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_PERIOD);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    dispose(mRunAxisDisposable);
//                    Logger.info("单轴运行结束");
//                }
//                return true;
//            }
//        });
//        mRunAxisElevationStopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().stopAxis();
//            }
//        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_control, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_send:
////                getMainActivity().getPresenter().send();
//                break;
//            case R.id.action_favourite:
//                getMainActivity().getPresenter().addToFavourite();
//                showAddFavouriteDialog();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshCurrentSpeed(int left, int right) {
        mCurrentLeftSpeedView.setText(left + "");
        mCurrentRightSpeedView.setText(right + "");
    }

    public void stopGetCurrentAngle() {
        if (mCurrentAngleDisposable != null && !mCurrentAngleDisposable.isDisposed()) {
            mCurrentAngleDisposable.dispose();
        }
    }

    public void startGetCurrentAngleTask() {
//        if (mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed()) {
////            mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
//
//
//
//
//
//            Observable.interval(0, 300, TimeUnit.MILLISECONDS)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Long>() {
//                        @Override
//                        public void onSubscribe(@NonNull Disposable d) {
//                            mCurrentAngleDisposable = d;
//                        }
//
//                        @Override
//                        public void onNext(@NonNull Long aLong) {
//                            Logger.warning("调用蓝牙接口：==>获取角度");
//                            getMainActivity().getPresenter().getBleService().writeData(getMainActivity().getPresenter().getModel().getAxisAngle());
//
//                        }
//
//                        @Override
//                        public void onError(@NonNull Throwable e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Logger.warning("onCom!!!!!!!!!!!");
//                        }
//                    });
//
////                    .subscribe(new Consumer<Long>() {
////                        @Override
////                        public void accept(@NonNull Long aLong) throws Exception {
////                            Logger.warning("调用蓝牙接口：==>获取角度");
////                            getMainActivity().getPresenter().getBleService().writeData(getMainActivity().getPresenter().getModel().getAxisAngle());
////                        }
////                    });
//






//        }
    }

    public void showCurrentAngle(String angle1, String angle2) {
        mCurrentSwingAngle = Double.valueOf(angle1);
        mCurrentElevationAngle = Double.valueOf(angle2);

        mCurrentElevationAngleView.setText(angle2);
        mCurrentSwingAngleView.setText(angle1);
    }

    public void showCurrentVoltage(String voltage) {
//        batteryView.setText(voltage + " V");
    }

//    public void showAddFavouriteDialog() {
//        long count = DataManager.getInstance().getDaoSession().getFavouriteRecordDao().count();
//
//        new MaterialDialog.Builder(getActivity())
//                .title("收藏")
//                .content("将当前的参数保存到收藏列表，您可以在「我的收藏」中看到所有的收藏内容。")
//                .input("为此收藏记录命名", "记录" + (count + 1), new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        getMainActivity().getPresenter().addToFavourite(input.toString(), mSwingAngle, mElevationAngle, mLeftSpeed, mRightSpeed);
//                    }
//                })
//                .positiveText("添加收藏")
//                .negativeText("取消")
//                .show();
//    }

    public void selectFromFavourite(long id) {
        if(id != 0) {
            final FavouriteRecord record = DataManager.getInstance().getDaoSession().getFavouriteRecordDao().load(id);

            mCurrentLeftSpeed = record.getLeftMotorSpeed();
            mCurrentRightSpeed = record.getRightMotorSpeed();

            //请求调整转速
            getMainActivity().getPresenter().setMotorSpeed(record.getLeftMotorSpeed(), record.getRightMotorSpeed());

            //延时发送调整角度命令，防止机器来不及处理，忽略命令
            RxUtils.timer(1).subscribe(new Consumer<Long>() {
                @Override
                public void accept(@NonNull Long aLong) throws Exception {
                    startGetCurrentAngleTask();
                    getMainActivity().getPresenter().setAxisAngle(record.getSwingAngle(), record.getElevationAngle());

                }
            });

            getMainActivity().refreshCurrentSpeed(record.getLeftMotorSpeed(), record.getRightMotorSpeed());
        } else {
            Logger.error("selectFromFavourite(), id = 0");
        }
    }

//    public void showSwingAngleDialog() {
//        new MaterialDialog.Builder(getActivity())
//                .title("设置摆角")
//                .content("摆角范围：(-90 ~ 90)")
//                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
//                .input("输入摆角", "", new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (input.length() == 0) {
//                            return;
//                        }
//                        double inputValue = Double.valueOf(input.toString());
//                        if (inputValue >= -90 && inputValue <= 90) {
//                            mSwingAngle = inputValue;
//                            mSwingAngleView.setText("摆角：" + input.toString());
//                        } else {
//                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
//                        }
//                    }
//                })
//                .positiveText("确定")
//                .show();
//    }

//    public void showElevationAngleDialog() {
//        new MaterialDialog.Builder(getActivity())
//                .title("设置仰角")
//                .content("仰角范围：(-90 ~ 50)")
//                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
//                .input("输入仰角", "", new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        if (input.length() == 0) {
//                            return;
//                        }
//                        double inputValue = Double.valueOf(input.toString());
//                        if (inputValue >= -90 && inputValue <= 50) {
//                            mElevationAngle = inputValue;
//                            mElevationAngleView.setText("仰角：" + input.toString());
//                        } else {
//                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
//                        }
//                    }
//                })
//                .positiveText("确定")
//                .show();
//    }

    public void showSetMotorSpeedDialog() {
        Logger.info("弹出对话框， leftspeed: " + mCurrentLeftSpeed + " rightspeed: " + mCurrentRightSpeed);
        final SetSpeedDialogWrapper ssd = new SetSpeedDialogWrapper(getMainActivity(), mCurrentLeftSpeed, mCurrentRightSpeed);
        ssd.showDialog();
        ssd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mCurrentLeftSpeed = ssd.getLeftSpeed();
                mCurrentRightSpeed = ssd.getRightSpeed();
                getMainActivity().refreshCurrentSpeed(mCurrentLeftSpeed, mCurrentRightSpeed);
            }
        });
    }

    public void showRightSpeedDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置右转速")
                .content("转速范围：(0 ~ 100)")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("输入转速", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.length() == 0) {
                            return;
                        }
                        int inputValue = Integer.valueOf(input.toString());
                        if (inputValue >= 0 && inputValue <= 100) {
                            mRightSpeed = Integer.valueOf(input.toString());
//                            mRightSpeedView.setText("右转速：" + input.toString());
                        } else {
                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }

    public double getSwingAngle() {
        return mSwingAngle;
    }

    public void setSwingAngle(double swingAngle) {
        this.mSwingAngle = swingAngle;
//        mSwingAngleView.setText("摆角：" + swingAngle);
    }

    public double getElevationAngle() {
        return mElevationAngle;
    }

    public void setElevationAngle(double elevationAngle) {
        this.mElevationAngle = elevationAngle;
//        mElevationAngleView.setText("仰角：" + elevationAngle);
    }

    public int getLeftSpeed() {
        return mLeftSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        this.mLeftSpeed = leftSpeed;
//        mLeftSpeedView.setText("左转速：" + leftSpeed);
    }

    public int getRightSpeed() {
        return mRightSpeed;
    }

    public void setRightSpeed(int rightSpeed) {
        this.mRightSpeed = rightSpeed;
//        mRightSpeedView.setText("右转速：" + rightSpeed);
    }

    private void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            Logger.warning("=====================停止获取角度任务!");
            disposable.dispose();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mRunAxisSwingPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
                //开始单轴连续调整时，启动一个获取当前角度的任务
                startGetCurrentAngleTask();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisSwingPosDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisSwingNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
                startGetCurrentAngleTask();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisSwingNegDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisElevationPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_PERIOD);
                startGetCurrentAngleTask();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisElevationPosDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisElevationNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_PERIOD);
                startGetCurrentAngleTask();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisElevationNegDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行任务结束");
            }
        }


//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            mRunAxisDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
//            return false;
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            dispose(mRunAxisDisposable);
//            Logger.info("单轴运行结束");
//            return true;
//        } else if(event.getActionButton()) {
//
//        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在页面结束时停止获取状态任务
        dispose(mStatusDisposable);
    }
}