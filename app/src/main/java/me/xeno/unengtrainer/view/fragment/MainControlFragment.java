package me.xeno.unengtrainer.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.presenter.MainPresenter;
import me.xeno.unengtrainer.util.DialogUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.RxUtils;
import me.xeno.unengtrainer.util.ToastUtils;
import me.xeno.unengtrainer.view.activity.BaseActivity;
import me.xeno.unengtrainer.view.activity.FavouriteActivity;
import me.xeno.unengtrainer.widget.RandomDialogWrapper;
import me.xeno.unengtrainer.widget.SetSpeedDialogWrapper;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class MainControlFragment extends BaseMainFragment implements View.OnTouchListener,MainPresenter.OnGetMotorSpeedListener {

    private View mRunAxisSwingPositiveView;
    private View mRunAxisSwingNegativeView;
    private View mRunAxisElevationPositiveView;
    private View mRunAxisElevationNegativeView;

    private View mStopElectricView;
//    private View mReturnToZeroView;
    private View mReturnToZero2View;

    private View mFromFavView;

    private View mSetMotorView;//设置左右电机速度
    private View mSetAngleView;//输入设置调整角度

    private View mRandomModeView;

    private TextView mCurrentSwingAngleView;
    private TextView mCurrentElevationAngleView;

    private View mSendView;

//    private double mSwingAngle;
//    private double mElevationAngle;
    private int mLeftSpeed;
    private int mRightSpeed;

    private TextView mCurrentRightSpeedView;
    private TextView mCurrentLeftSpeedView;

    private TextView batteryView;

    private TextView mDebugTextView;

    private int pick;

    private Disposable mBatteryDisposable;
    private Disposable mCurrentAngleInfrequentlyDisposable;

    private Disposable mCurrentAngleDisposable;//快速获取角度任务

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
        if (mBatteryDisposable != null && !mBatteryDisposable.isDisposed()) {
        } else {
            mBatteryDisposable = getMainActivity().getPresenter().getBatteryVoltage(Config.GET_BATTERY_PERIOD);
        }
        if (mStatusDisposable != null && !mStatusDisposable.isDisposed()) {
        } else {
            mStatusDisposable = getMainActivity().getPresenter().startGetStatusTask(Config.GET_STATUS_PERIOD);
        }

        //开始一个获取角度的任务
        if(mCurrentAngleInfrequentlyDisposable != null && !mCurrentAngleInfrequentlyDisposable.isDisposed()) {
        } else {
            mCurrentAngleInfrequentlyDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD_INFREQUENTLY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //页面休眠时，停止获取电压
        Logger.info("页面休眠，获取电压循环任务已停止");
        dispose(mBatteryDisposable);
        dispose(mCurrentAngleInfrequentlyDisposable);
//        dispose(mStatusDisposable);

        //TODO 看看要不要在休眠时让获取状态任务也暂停，以节省电量
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

        mDebugTextView = (TextView) root.findViewById(R.id.debug_text_view);

        mSetMotorView =  root.findViewById(R.id.set_motor_speed);
        mSetAngleView =  root.findViewById(R.id.set_angle);

        mStopElectricView = root.findViewById(R.id.stop_both_electric);
//        mReturnToZeroView = root.findViewById(R.id.return_to_zero);
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

        batteryView = (TextView) root.findViewById(R.id.tv_battery);

        mRandomModeView = root.findViewById(R.id.random_mode);
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
                getMainActivity().getPresenter().setCurrentLeftSpeed(0);
                getMainActivity().getPresenter().setCurrentRightSpeed(0);
                getMainActivity().getPresenter().setMotorSpeed(0, 0);
//                getMainActivity().refreshCurrentSpeed(0, 0);
            }
        });
//        mReturnToZeroView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogUtils.dialog(getMainActivity(), "恢复零位", "确定要恢复到0度位置吗？",
//                        "恢复", "取消", new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
//                                dialog.dismiss();
//                                getMainActivity().getPresenter().setAxisAngle(0, 0);
//                            }
//                        }, new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@android.support.annotation.NonNull MaterialDialog dialog, @android.support.annotation.NonNull DialogAction which) {
//                                dialog.dismiss();
//                            }
//                        });
//            }
//        });

        mRunAxisSwingPositiveView.setOnTouchListener(this);
        mRunAxisSwingNegativeView.setOnTouchListener(this);
        mRunAxisElevationPositiveView.setOnTouchListener(this);
        mRunAxisElevationNegativeView.setOnTouchListener(this);

        mRandomModeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME 刚连上蓝牙的时候角度还没有获取到，不能弹出dialog，不然会空指针
                showRandomModeDialog();
            }
        });

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


        //进入控制界面后两秒获取一次当前角度信息
        initCurrentAngle();

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

    private void initCurrentAngle() {
        RxUtils.timer(3).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
               getMainActivity().getPresenter().getAxisAngle();

            }
        });
    }

//    public void stopGetCurrentAngle() {
//        if (mCurrentAngleDisposable != null && !mCurrentAngleDisposable.isDisposed()) {
//            mCurrentAngleDisposable.dispose();
//        }
//    }

    public void showCurrentAngle(String angle1, String angle2) {
//        mCurrentSwingAngle = Double.valueOf(angle1);
//        mCurrentElevationAngle = Double.valueOf(angle2);

        mCurrentElevationAngleView.setText(angle2);
        mCurrentSwingAngleView.setText(angle1);
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

            //TODO 应该异步，在机器设置成功以后再同步速度显示
            getMainActivity().getPresenter().setCurrentLeftSpeed(record.getLeftMotorSpeed());
            getMainActivity().getPresenter().setCurrentRightSpeed(record.getRightMotorSpeed());

            //请求调整转速
            getMainActivity().getPresenter().setMotorSpeed(record.getLeftMotorSpeed(), record.getRightMotorSpeed());

            //延时发送调整角度命令，防止机器来不及处理，忽略命令
            RxUtils.timer(1).subscribe(new Consumer<Long>() {
                @Override
                public void accept(@NonNull Long aLong) throws Exception {
                    getMainActivity().getPresenter().setAxisAngle(record.getSwingAngle(), record.getElevationAngle());
                }
            });

//            getMainActivity().refreshCurrentSpeed(record.getLeftMotorSpeed(), record.getRightMotorSpeed());
        } else {
            Logger.error("selectFromFavourite(), id = 0");
        }
    }


    public void showSetMotorSpeedDialog() {

        if(Config.DEBUG_MODE == Config.DEBUG_MODE_CTRL) {
            onGetSpeed();
        } else {
            //请求电机速度，异步弹出Dialog
            getMainActivity().getPresenter().getMotorSpeed();
        }
           //弹出对话框前先获取一次速度

    }

    @Override
    public void onGetSpeed() {
        float currentLeftSpeed = getMainActivity().getPresenter().getCurrentLeftSpeed();
        float currentRightSpeed = getMainActivity().getPresenter().getCurrentRightSpeed();
        Logger.info("弹出对话框， leftspeed: " + currentLeftSpeed + " rightspeed: " + currentRightSpeed);
        final SetSpeedDialogWrapper ssd = new SetSpeedDialogWrapper(getMainActivity(), currentLeftSpeed, currentRightSpeed);
        ssd.showDialog();
        ssd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getMainActivity().getPresenter().setCurrentLeftSpeed(ssd.getLeftSpeed());
                getMainActivity().getPresenter().setCurrentRightSpeed(ssd.getRightSpeed());
//                getMainActivity().refreshCurrentSpeed(mCurrentLeftSpeed, mCurrentRightSpeed);
            }
        });
    }

    public void showRandomModeDialog() {
        RandomDialogWrapper rdw = new RandomDialogWrapper(getMainActivity(), getMainActivity().getPresenter().getCurrentElevationAngle() + "", getMainActivity().getPresenter().getCurrentSwingAngle()+ "");
        rdw.showDialog();
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

//    public double getSwingAngle() {
//        return mSwingAngle;
//    }
//
//    public void setSwingAngle(double swingAngle) {
//        this.mSwingAngle = swingAngle;
////        mSwingAngleView.setText("摆角：" + swingAngle);
//    }
//
//    public double getElevationAngle() {
//        return mElevationAngle;
//    }
//
//    public void setElevationAngle(double elevationAngle) {
//        this.mElevationAngle = elevationAngle;
////        mElevationAngleView.setText("仰角：" + elevationAngle);
//    }

    private void dispose(Disposable disposable) {
        if(disposable == mCurrentAngleDisposable) {
            Logger.error("尝试销毁快速获取角度任务");
        }
        if (disposable != null && !disposable.isDisposed()) {
            if(Config.isDebugging()) {
                ToastUtils.toast(getActivity().getApplicationContext(), "循环任务已停止");
            }
            disposable.dispose();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == mRunAxisSwingPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);

                //只有在此引用没有值或已被回收时，才开启新任务，确保不会因为发生A任务启动->引用，B任务启动->引用，B任务销毁，A任务产生逃逸现象出现
                if(mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed())
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisSwingPosDisposable);
                //抬起时销毁快速获取角度任务
                dispose(mCurrentAngleDisposable);

                Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisSwingNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisSwingNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_STOP, Config.RUN_AXIS_PERIOD);
                //只有在此引用没有值或已被回收时，才开启新任务，确保不会因为发生A任务启动->引用，B任务启动->引用，B任务销毁，A任务产生逃逸现象出现
                if(mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed())
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisSwingNegDisposable);
                //抬起时销毁快速获取角度任务
                dispose(mCurrentAngleDisposable);

                Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisElevationPositiveView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationPosDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE, Config.RUN_AXIS_PERIOD);
                //只有在此引用没有值或已被回收时，才开启新任务，确保不会因为发生A任务启动->引用，B任务启动->引用，B任务销毁，A任务产生逃逸现象出现
                if(mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed())
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisElevationPosDisposable);
                //抬起时销毁快速获取角度任务
                dispose(mCurrentAngleDisposable);
                     Logger.info("单轴运行任务结束");
            }
        }
        if (v == mRunAxisElevationNegativeView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mRunAxisElevationNegDisposable = getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_NEGATIVE, Config.RUN_AXIS_PERIOD);
                //只有在此引用没有值或已被回收时，才开启新任务，确保不会因为发生A任务启动->引用，B任务启动->引用，B任务销毁，A任务产生逃逸现象出现
                if(mCurrentAngleDisposable == null || mCurrentAngleDisposable.isDisposed())
                    mCurrentAngleDisposable = getMainActivity().getPresenter().startGetAxisAngleTask(Config.GET_ANGLE_PERIOD);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                getMainActivity().getPresenter().stopAxis();
                dispose(mRunAxisElevationNegDisposable);
                //抬起时销毁快速获取角度任务
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

    public void showDebugRssi(int rssi) {
        mDebugTextView.setText(String.valueOf(rssi));
    }


}