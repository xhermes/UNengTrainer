package me.xeno.unengtrainer.view.fragment;

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
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class MainControlFragment extends BaseMainFragment implements View.OnTouchListener {

    private View mRunAxisSwingPositiveView;
    private View mRunAxisSwingNegativeView;
    private View mRunAxisElevationPositiveView;
    private View mRunAxisElevationNegativeView;

    private View mLeftMotorView;
    private View mRightMotorView;

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
            getMainActivity().getPresenter().startGetStatusTask(Config.GET_STATUS_PERIOD);
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

//        input = (EditText) root.findViewById(R.id.input);
        mLeftMotorView =  root.findViewById(R.id.set_left);
        mRightMotorView =  root.findViewById(R.id.set_right);
//        stop = (TextView) root.findViewById(R.id.stop);
//        mWrite1View = (TextView) root.findViewById(R.id.write_1);
//        mWrite2View = (TextView) root.findViewById(R.id.write_2);
//        makeZero = (TextView) root.findViewById(R.id.make_zero);
//        tv_angle = (TextView) root.findViewById(R.id.angle);
//        tv_voltage = (TextView) root.findViewById(R.id.battery);
//        batteryView = (TextView) root.findViewById(R.id.tv_battery);
//
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
////        mElevationAngleBar = (AppCompatSeekBar) root.findViewById(R.id.seek_elevation_angle);
////        mSwingAngleBar = (AppCompatSeekBar) root.findViewById(R.id.seek_swing_angle);
////        mLeftSpeedBar = (AppCompatSeekBar) root.findViewById(R.id.seek_left_speed);
////        mRightSpeedBar = (AppCompatSeekBar) root.findViewById(R.id.seek_right_speed);
////
////        mElevationAngleEdt = (AppCompatEditText) root.findViewById(R.id.edit_elevation_angle);
////        mSwingAngleEdt = (AppCompatEditText) root.findViewById(R.id.edit_swing_angle);
////        mLeftSpeedEdt = (AppCompatEditText) root.findViewById(R.id.edit_left_motor);
////        mRightSpeedEdt = (AppCompatEditText) root.findViewById(R.id.edit_right_speed);
//
//        mElevationAngleView = (TextView) root.findViewById(R.id.elevation_angle);
//        mSwingAngleView = (TextView) root.findViewById(R.id.swing_angle);
//        mLeftSpeedView = (TextView) root.findViewById(R.id.left_speed);
//        mRightSpeedView = (TextView) root.findViewById(R.id.right_speed);
//
//        mCurrentRightSpeedView = (TextView) root.findViewById(R.id.current_right_speed);
//        mCurrentLeftSpeedView = (TextView) root.findViewById(R.id.current_left_speed);
//
//        mSendView = root.findViewById(R.id.send);
//
//        initView();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    private void initView() {

        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);

                getMainActivity().getPresenter().send(mSwingAngle, mElevationAngle, mLeftSpeed, mRightSpeed);
            }
        });
//        mWrite1View.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Observable.interval(0,50, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<Long>() {
//                            @Override
//                            public void onSubscribe(@NonNull Disposable d) {
//                                cd.add(d);
//                            }
//
//                            @Override
//                            public void onNext(@NonNull Long aLong) {
//                                Logger.info(aLong + "");
//                                getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE);
//                            }
//
//                            @Override
//                            public void onError(@NonNull Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
////
//            }
//        });
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_STOP);
//                cd.clear();
//            }
//        });
//
//        mWrite2View.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().setAxisAngle(Double.valueOf(input.getText().toString()), Double.valueOf(input.getText().toString()));
//            }
//        });
//        makeZero.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().setMotorSpeed(Integer.valueOf(motor1.getText().toString()),Integer.valueOf(motor2.getText().toString()));
//            }
//        });
//
//        tv_angle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().getAxisAngle();
//            }
//        });
//        tv_voltage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getMainActivity().getPresenter().getBatteryVoltage();
//            }
//        });
//        mRunAxis1View.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pick = 100;
//                new MaterialDialog.Builder(getMainActivity())
//                        .items("第一轴", "第二轴")
//                        .alwaysCallSingleChoiceCallback()
//                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                                pick = which;
//                                return true;
//                            }
//                        })
//                        .cancelable(true)
//                        .negativeText("取消")
//                        .positiveText("确定")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
//                                if(pick != 100) {
//                                    if(pick == 0) {
//                                       //第一轴
//                                        mDisposable1 = Observable.just(1).repeat()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(Schedulers.io())
//                                                .subscribe(new Consumer<Integer>() {
//                                                    @Override
//                                                    public void accept(@NonNull Integer integer) throws Exception {
//                                                        getMainActivity().getPresenter().runAxis(1, 0);
//                                                    }
//                                                });
//
//                                    } else if(pick == 1) {
//                                        //第二轴
//                                        mDisposable2 = Observable.just(1).repeat()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(Schedulers.io())
//                                                .subscribe(new Consumer<Integer>() {
//                                                    @Override
//                                                    public void accept(@NonNull Integer integer) throws Exception {
//                                                        getMainActivity().getPresenter().runAxis(0, 1);
//                                                    }
//                                                });
//
//                                    }
//                                }
//                            }
//                        })
//                        .show();
//            }
//        });
//        mRunAxis2View.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pick = 100;
//                new MaterialDialog.Builder(getMainActivity())
//                        .items("第一轴", "第二轴")
//                        .alwaysCallSingleChoiceCallback()
//                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                                pick = which;
//                                return true;
//                            }
//                        })
//                        .cancelable(true)
//                        .negativeText("取消")
//                        .positiveText("确定")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
//                                if(pick != 100) {
//                                    if(pick == 0) {
//                                        //第一轴
//                                        mDisposable1 = Observable.just(1).repeat()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(Schedulers.io())
//                                                .subscribe(new Consumer<Integer>() {
//                                                    @Override
//                                                    public void accept(@NonNull Integer integer) throws Exception {
//                                                        getMainActivity().getPresenter().runAxis(2, 0);
//                                                    }
//                                                });
//
//                                    } else if(pick == 1) {
//                                        //第二轴
//                                        mDisposable1 = Observable.just(1).repeat()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(Schedulers.io())
//                                                .subscribe(new Consumer<Integer>() {
//                                                    @Override
//                                                    public void accept(@NonNull Integer integer) throws Exception {
//                                                        getMainActivity().getPresenter().runAxis(0, 2);
//                                                    }
//                                                });
//
//                                    }
//                                }
//                            }
//                        })
//                        .show();
//            }
//        });
//        mRunAxisStopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        mRunAxisSwingPositiveView.setOnTouchListener(this);
        mRunAxisSwingNegativeView.setOnTouchListener(this);
        mRunAxisElevationPositiveView.setOnTouchListener(this);
        mRunAxisElevationNegativeView.setOnTouchListener(this);
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

    public void showCurrentAngle(String angle1, String angle2) {
        mCurrentSwingAngle = Double.valueOf(angle1);
        mCurrentElevationAngle = Double.valueOf(angle2);

        mCurrentElevationAngleView.setText(angle2);
        mCurrentSwingAngleView.setText(angle1);
    }

    public void showCurrentVoltage(String voltage) {
//        batteryView.setText(voltage + " V");
    }

    public void showAddFavouriteDialog() {
        long count = DataManager.getInstance().getDaoSession().getFavouriteRecordDao().count();

        new MaterialDialog.Builder(getActivity())
                .title("收藏")
                .content("将当前的参数保存到收藏列表，您可以在「我的收藏」中看到所有的收藏内容。")
                .input("为此收藏记录命名", "记录" + (count + 1), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        getMainActivity().getPresenter().addToFavourite(input.toString(), mSwingAngle, mElevationAngle, mLeftSpeed, mRightSpeed);
                    }
                })
                .positiveText("添加收藏")
                .negativeText("取消")
                .show();
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

    public void showLeftSpeedDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置左转速")
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
                            mLeftSpeed = Integer.valueOf(input.toString());
//                            mLeftSpeedView.setText("左转速：" + input.toString());
                        } else {
                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
                        }
                    }
                })
                .positiveText("确定")
                .show();
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
            disposable.dispose();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mRunAxisSwingPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
                //开始单轴连续调整时，启动一个获取当前角度的任务
                if (mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed()) {
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                dispose(mRunAxisSwingPosDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行结束");
            }
        }
        if (v == mRunAxisSwingNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
                if (mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed()) {
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                dispose(mRunAxisSwingNegDisposable);
                dispose(mCurrentAngleDisposable);
                Logger.info("单轴运行结束");
            }
        }
        if (v == mRunAxisElevationPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_PERIOD);
                if (mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed()) {
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                dispose(mRunAxisElevationPosDisposable);
                Logger.info("单轴运行结束");
            }
        }
        if (v == mRunAxisElevationNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_PERIOD);
                if (mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed()) {
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                dispose(mRunAxisElevationNegDisposable);
                Logger.info("单轴运行结束");
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
}