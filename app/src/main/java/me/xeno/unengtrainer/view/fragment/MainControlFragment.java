package me.xeno.unengtrainer.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.util.Logger;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class MainControlFragment extends BaseMainFragment {

    private TextView mDescription;
    private TextView mWrite1View;
    private TextView mWrite2View;
    private TextView tv_angle;
    private TextView tv_voltage;
    private TextView angleView;
    private TextView batteryView;
    private TextView stop;

    private EditText motor1;
    private EditText motor2;
    private EditText input;
    private TextView makeZero;
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

        input = (EditText) root.findViewById(R.id.input);
        motor1 = (EditText) root.findViewById(R.id.motor1);
        motor2 = (EditText) root.findViewById(R.id.motor2);
        stop = (TextView) root.findViewById(R.id.stop);
        mWrite1View = (TextView) root.findViewById(R.id.write_1);
        mWrite2View = (TextView) root.findViewById(R.id.write_2);
        makeZero = (TextView) root.findViewById(R.id.make_zero);
        tv_angle = (TextView) root.findViewById(R.id.angle);
        tv_voltage = (TextView) root.findViewById(R.id.battery);
        angleView = (TextView) root.findViewById(R.id.tv_angle);
        batteryView = (TextView) root.findViewById(R.id.tv_battery);

        initView();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    private void initView() {
        mWrite1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Observable.interval(0,50, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                cd.add(d);
                            }

                            @Override
                            public void onNext(@NonNull Long aLong) {
                                Logger.info(aLong + "");
                                getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_POSITIVE);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
//
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_STOP);
                cd.clear();
            }
        });

        mWrite2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().setAxisAngle(Double.valueOf(input.getText().toString()), Double.valueOf(input.getText().toString()));
            }
        });
        makeZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().setMotorSpeed(Integer.valueOf(motor1.getText().toString()),Integer.valueOf(motor2.getText().toString()));
            }
        });

        tv_angle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().getAxisAngle();
            }
        });
        tv_voltage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().getPresenter().getBatteryVoltage();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_control, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
//                getMainActivity().getPresenter().send();
                break;
            case R.id.action_favourite:
//                getMainActivity().getPresenter().addToFavourite();
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



}