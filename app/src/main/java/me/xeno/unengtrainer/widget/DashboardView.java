package me.xeno.unengtrainer.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.ParseException;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.model.BatteryModel;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.util.TimeUtils;

/**
 * Created by xeno on 2017/7/27.
 */

public class DashboardView extends LinearLayout {

    private String mSwingAngle = "0";
    private String mElevationAngle = "0";
    private float mLeftSpeed;
    private float mRightSpeed;

    private TextView mLeftMotorSpeedView;
    private TextView mRightMotorSpeedView;
    private TextView mElevationAngleView;
    private TextView mSwingAngleView;

    private View mAddToFavView;

    private TextView batteryView;

    private  AppCompatEditText inputView;


    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_dashboard, this);

        batteryView = (TextView) findViewById(R.id.tv_battery);

        mLeftMotorSpeedView = (TextView) findViewById(R.id.current_left_speed);
        mRightMotorSpeedView = (TextView) findViewById(R.id.current_right_speed);
        mElevationAngleView = (TextView) findViewById(R.id.current_elevation_angle);
        mSwingAngleView = (TextView) findViewById(R.id.current_swing_angle);

        mAddToFavView = findViewById(R.id.fav);

        mAddToFavView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFavouriteDialog();
            }
        });
    }

    public void setCurrentLeftSpeed(float speed) {
        mLeftSpeed = speed;
        mLeftMotorSpeedView.setText(speed + "%");
    }

    public void setCurrentRightSpeed(float speed) {
        mRightSpeed = speed;
        mRightMotorSpeedView.setText(speed + "%");
    }

    public void setCurrentSwingAngle(String angle) {
        mSwingAngle = angle;
        mSwingAngleView.setText(angle + "°");
    }

    public void setCurrentElevationAngle(String angle) {
        mElevationAngle = angle;
        mElevationAngleView.setText(angle + "°");
    }

    public void addToFavourite(String name, double angle1, double angle2, float speed1, float speed2) {
        try {
            String currentTime = TimeUtils.longToString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
            FavouriteRecord record = new FavouriteRecord();
//            name, currentTime, currentTime, angle1, angle2, speed1, speed2
            record.setName(name);
            record.setCreateTime(currentTime);
            record.setModifyTime(currentTime);
            record.setSwingAngle(angle1);
            record.setElevationAngle(angle2);
            record.setLeftMotorSpeed(speed1);
            record.setRightMotorSpeed(speed2);
            DataManager.getInstance().getDaoSession().getFavouriteRecordDao().insert(record);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void showAddFavouriteDialog() {
        long count = DataManager.getInstance().getDaoSession().getFavouriteRecordDao().count();

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("收藏")
                .customView(R.layout.view_add_to_favourite, false)
                .positiveText("添加收藏")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addToFavourite(inputView.getText().toString(), Double.valueOf(mSwingAngle), Double.valueOf(mElevationAngle), mLeftSpeed, mRightSpeed);
                    }
                })
                .build();
//                .input("为此收藏记录命名", "记录" + (count + 1), new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        addToFavourite(input.toString(), Double.valueOf(mSwingAngle), Double.valueOf(mElevationAngle), mLeftSpeed, mRightSpeed);
//                    }
//                })

        TextView leftSpeedView = (TextView) dialog.findViewById(R.id.left_speed);
        TextView rightSpeedView = (TextView) dialog.findViewById(R.id.right_speed);
        TextView swingAngleView = (TextView) dialog.findViewById(R.id.swing_angle);
        TextView elevationView = (TextView) dialog.findViewById(R.id.elevation_angle);
        inputView = (AppCompatEditText) dialog.findViewById(R.id.record_name_edt) ;

        inputView.setHint("输入名称");
        inputView.setText("记录" + ((count + 1)));

        leftSpeedView.setText(mLeftSpeed + "%");
        rightSpeedView.setText(mRightSpeed + "%");
        swingAngleView.setText(mSwingAngle + "°");
        elevationView.setText(mElevationAngle + "°");

        dialog.show();
    }

    public void showCurrentVoltage(String voltage) {
        if(batteryView != null)
            batteryView.setText(voltage + "V " +BatteryModel.getCurrentBatteryPercentage(voltage));
    }
}
