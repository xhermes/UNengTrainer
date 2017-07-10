package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.ToastUtils;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class MainControlFragment extends BaseMainFragment {

//    private TextView mDescription;
//    private TextView mWrite1View;
//    private TextView mWrite2View;
//    private TextView tv_angle;
//    private TextView tv_voltage;
    private TextView angleView;
    private TextView batteryView;
//    private TextView stop;
//
//    private EditText motor1;
//    private EditText motor2;
//    private EditText input;
//    private TextView makeZero;

    private AppCompatSeekBar mElevationAngleBar;
    private AppCompatSeekBar mSwingAngleBar;
    private AppCompatSeekBar mLeftSpeedBar;
    private AppCompatSeekBar mRightSpeedBar;

    private EditText mElevationAngleEdt;
    private EditText mSwingAngleEdt;
    private EditText mLeftSpeedEdt;
    private EditText mRightSpeedEdt;

    private TextView mElevationAngleView;
    private TextView mSwingAngleView;
    private TextView mLeftSpeedView;
    private TextView mRightSpeedView;

    private View mSendView;

    private double mSwingAngle;
    private double mElevationAngle;
    private int mLeftSpeed;
    private int mRightSpeed;

    private final CompositeDisposable cd =new CompositeDisposable();

    public static MainControlFragment newInstance() {
        return new MainControlFragment();
    }

    public MainControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



//        mFloatingActionBtn =
//                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
//        fab.setImageResource(R.drawable.ic_done);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_control_main, container, false);

//        input = (EditText) root.findViewById(R.id.input);
//        motor1 = (EditText) root.findViewById(R.id.motor1);
//        motor2 = (EditText) root.findViewById(R.id.motor2);
//        stop = (TextView) root.findViewById(R.id.stop);
//        mWrite1View = (TextView) root.findViewById(R.id.write_1);
//        mWrite2View = (TextView) root.findViewById(R.id.write_2);
//        makeZero = (TextView) root.findViewById(R.id.make_zero);
//        tv_angle = (TextView) root.findViewById(R.id.angle);
//        tv_voltage = (TextView) root.findViewById(R.id.battery);
        angleView = (TextView) root.findViewById(R.id.tv_angle);
        batteryView = (TextView) root.findViewById(R.id.tv_battery);

//        mElevationAngleBar = (AppCompatSeekBar) root.findViewById(R.id.seek_elevation_angle);
//        mSwingAngleBar = (AppCompatSeekBar) root.findViewById(R.id.seek_swing_angle);
//        mLeftSpeedBar = (AppCompatSeekBar) root.findViewById(R.id.seek_left_speed);
//        mRightSpeedBar = (AppCompatSeekBar) root.findViewById(R.id.seek_right_speed);
//
//        mElevationAngleEdt = (AppCompatEditText) root.findViewById(R.id.edit_elevation_angle);
//        mSwingAngleEdt = (AppCompatEditText) root.findViewById(R.id.edit_swing_angle);
//        mLeftSpeedEdt = (AppCompatEditText) root.findViewById(R.id.edit_left_motor);
//        mRightSpeedEdt = (AppCompatEditText) root.findViewById(R.id.edit_right_speed);

        mElevationAngleView = (TextView) root.findViewById(R.id.elevation_angle);
        mSwingAngleView = (TextView) root.findViewById(R.id.swing_angle);
        mLeftSpeedView = (TextView) root.findViewById(R.id.left_speed);
        mRightSpeedView = (TextView) root.findViewById(R.id.right_speed);

        mSendView = root.findViewById(R.id.send);

        initView();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    private void initView() {
        mElevationAngleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showElevationAngleDialog();
            }
        });
        mSwingAngleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSwingAngleDialog();
            }
        });
        mLeftSpeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeftSpeedDialog();
            }
        });
        mRightSpeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRightSpeedDialog();
            }
        });


        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            case R.id.action_favourite:
//                getMainActivity().getPresenter().addToFavourite();
                showAddFavouriteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showCurrentAngle(String angle1, String angle2) {
        angleView.setText("1轴角度：" + angle1 + "2轴角度：" + angle2);
    }

    public void showCurrentVoltage(String voltage) {
        batteryView.setText("当前电压：" + voltage + " V");
    }

    public void showAddFavouriteDialog() {
        long count = DataManager.getInstance().getDaoSession().getFavouriteRecordDao().count();

        new MaterialDialog.Builder(getActivity())
                .title("收藏")
                .content("将当前的参数保存到收藏列表，您可以在「我的收藏」中看到所有的收藏内容。")
                .input("为此收藏记录命名", "记录" + (count + 1), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        getMainActivity().getPresenter().addToFavourite(input.toString(), mSwingAngle,mElevationAngle,mLeftSpeed,mRightSpeed);
                    }
                })
                .positiveText("添加收藏")
                .negativeText("取消")
                .show();
    }

    public void showSwingAngleDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置摆角")
                .content("摆角范围：(-90 ~ 90)")
                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED)
                .input("输入摆角", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if(input.length() == 0) {
                            return;
                        }
                        double inputValue = Double.valueOf(input.toString());
                        if(inputValue >= -90 && inputValue <= 90) {
                            mSwingAngle = inputValue;
                            mSwingAngleView.setText("摆角：" + input.toString());
                        } else {
                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }
    public void showElevationAngleDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置仰角")
                .content("仰角范围：(-90 ~ 50)")
                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED)
                .input("输入仰角", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if(input.length() == 0) {
                            return;
                        }
                        double inputValue = Double.valueOf(input.toString());
                        if(inputValue >= -90 && inputValue <= 50) {
                            mElevationAngle = inputValue;
                            mElevationAngleView.setText("仰角：" + input.toString());
                        } else {
                            ToastUtils.toast(getActivity().getApplicationContext(), "输入超限！");
                        }
                    }
                })
                .positiveText("确定")
                .show();
    }
    public void showLeftSpeedDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("设置左转速")
                .content("转速范围：(0 ~ 100)")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("输入转速", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if(input.length() == 0) {
                            return;
                        }
                        int inputValue = Integer.valueOf(input.toString());
                        if (inputValue >= 0 && inputValue <= 100) {
                            mLeftSpeed = Integer.valueOf(input.toString());
                            mLeftSpeedView.setText("左转速：" + input.toString());
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
                        if(input.length() == 0) {
                            return;
                        }
                        int inputValue = Integer.valueOf(input.toString());
                        if (inputValue >= 0 && inputValue <= 100) {
                            mRightSpeed = Integer.valueOf(input.toString());
                            mRightSpeedView.setText("右转速：" + input.toString());
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
        this.mSwingAngle = mSwingAngle;
        mSwingAngleView.setText("摆角：" + swingAngle);
    }

    public double getElevationAngle() {
        return mElevationAngle;
    }

    public void setElevationAngle(double elevationAngle) {
        this.mElevationAngle = mElevationAngle;
        mElevationAngleView.setText("仰角：" + elevationAngle);
    }

    public int getLeftSpeed() {
        return mLeftSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        this.mLeftSpeed = mLeftSpeed;
        mLeftSpeedView.setText("左转速：" + leftSpeed);
    }

    public int getRightSpeed() {
        return mRightSpeed;
    }

    public void setRightSpeed(int rightSpeed) {
        this.mRightSpeed = mRightSpeed;
        mRightSpeedView.setText("右转速：" + rightSpeed);
    }
}