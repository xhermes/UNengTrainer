package me.xeno.unengtrainer.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.scan.ListScanCallback;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.model.entity.EnableWrapper;
import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.model.entity.GetAxisAngleWrapper;
import me.xeno.unengtrainer.model.entity.GetBatteryVoltageWrapper;
import me.xeno.unengtrainer.model.entity.GetStatusWrapper;
import me.xeno.unengtrainer.model.entity.MakeZeroCompletedWrapper;
import me.xeno.unengtrainer.model.entity.RunAxisWrapper;
import me.xeno.unengtrainer.model.entity.SetAxisAngleWrapper;
import me.xeno.unengtrainer.model.entity.SetAxisSpeedWrapper;
import me.xeno.unengtrainer.model.entity.SetMotorSpeedWrapper;
import me.xeno.unengtrainer.model.entity.TurnBrakeWrapper;
import me.xeno.unengtrainer.service.BleService;
import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.DialogUtils;
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.util.SpUtils;
import me.xeno.unengtrainer.util.TimeUtils;
import me.xeno.unengtrainer.view.activity.MainActivity;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by xeno on 2017/6/21.
 */

public class MainPresenter {

    private static final int SCAN_TIME_OUT_MILLIS = 10000;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    private MainActivity mActivity;
    private BluetoothModel mModel;

    private BleService mBleService;

    private BleServiceListener mListener = new BleServiceListener() {
        @Override
        public void onGetStatus(GetStatusWrapper wrapper) {

            GetStatusWrapper.AxisStatus axisStatus1 = wrapper.getAxisStatuses()[0];
            GetStatusWrapper.AxisStatus axisStatus2 = wrapper.getAxisStatuses()[1];

            StringBuilder sb = new StringBuilder();
            sb.append("运行状态：").append(axisStatus1.getRunning()).append(axisStatus2.getRunning()).append("\n")
                    .append("急停：").append(axisStatus1.getAbruptStopping()).append(axisStatus2.getAbruptStopping()).append("\n")
                    .append("报警位：").append(axisStatus1.getAlerting()).append(axisStatus2.getAlerting()).append("\n")
                    .append("正限位：").append(axisStatus1.getPositiveSpacing()).append(axisStatus2.getPositiveSpacing()).append("\n")
                    .append("负限位：").append(axisStatus1.getNegativeSpacing()).append(axisStatus2.getNegativeSpacing()).append("\n");

            String content = sb.toString();

            new MaterialDialog.Builder(mActivity)
                    .title("查询步进电机状态回复")
                    .content(content)
                    .show();
        }

        @Override
        public void onEnable(EnableWrapper wrapper) {

        }

        @Override
        public void onTurnBrake(TurnBrakeWrapper wrapper) {

        }

        @Override
        public void onMakeZeroCompleted(MakeZeroCompletedWrapper wrapper) {

        }

        @Override
        public void onSetAxisAngle(SetAxisAngleWrapper wrapper) {

        }

        @Override
        public void onRunAxis(RunAxisWrapper wrapper) {

        }

        @Override
        public void onSetAxisSpeed(SetAxisSpeedWrapper wrapper) {

        }

        @Override
        public void onGetAxisAngle(GetAxisAngleWrapper wrapper) {
            mActivity.displayAngle(wrapper.getAxis1Angle(), wrapper.getAxis2Angle());
        }

        @Override
        public void onSetMotorSpeed(SetMotorSpeedWrapper wrapper) {

        }

        @Override
        public void onGetBatteryVoltage(GetBatteryVoltageWrapper wrapper) {
            mActivity.displayBattery(wrapper.getVoltage());
        }
    };


    /**
     * 向机器发送调整姿态命令，参数由用户在界面中设置好
     * 速度分成 100 等份，超过100不执行
     *
     * @param angle1 第一轴角度
     * @param angle2 第二轴角度
     * @param speed1 电机1转速
     * @param speed2 电机2转速
     */
    public void send(double angle1, double angle2, int speed1, int speed2) {
        Logger.info("send:=======> angle1=" + angle1 + " angle2=" + angle2 + "speed1=" + speed1 + "speed2=" + speed2);
        setAxisAngle(angle1, angle2);
        setMotorSpeed(speed1, speed2);
    }

    public void addToFavourite(String name, double angle1, double angle2, int speed1, int speed2) {
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

    /**
     * @param angle1 第一轴角度
     * @param angle2 第二轴角度
     */
    public void setAxisAngle(double angle1, double angle2) {
        mBleService.writeData(mModel.setAxisAngle(angle1, angle2));
    }

    public void makeZero() {
        mBleService.writeData(mModel.makeZero());
    }

    /**
     * 速度分成 100 等份，超过100不执行
     *
     * @param motor1 电机1
     * @param motor2 电机2
     */
    public void setMotorSpeed(int motor1, int motor2) {
        mBleService.writeData(mModel.setMotorSpeed(motor1, motor2));
    }


    /**
     * 1,2 轴单轴运行/停止
     * 0:第一轴停止运行
     * 1:第一轴正方向连续运行(直到正限位或者报警)
     * 2:第一轴负方向连续运行(直到负限位或者报警)
     *
     * @param axis1 第一轴
     * @param axis2 第二轴
     * @return a rxjava disposable for unsubscribing
     */
    public Disposable runAxis(final int axis1, final int axis2, int period) {
        return Observable.interval(0, period, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        mBleService.writeData(mModel.runAxis(axis1, axis2));
                    }
                });
    }

    public void stopAxis() {
        mBleService.writeData(mModel.runAxis(Config.RUN_AXIS_STOP, Config.RUN_AXIS_STOP));
    }



    public void getBatteryVoltage() {
        if (mModel != null) {
            //TODO 调试用
            byte[] frame = mModel.getBatteryVoltage();
            String str = CommonUtils.bytes2HexString(frame);
            DialogUtils.logDialog(mActivity, str);
            mBleService.writeData(mModel.getBatteryVoltage());
        }
    }

    public void getAxisAngle() {
        if (mModel != null) {
            //TODO 调试用
            byte[] frame = mModel.getAxisAngle();
            String str = CommonUtils.bytes2HexString(frame);
            DialogUtils.logDialog(mActivity, str);
            mBleService.writeData(mModel.getAxisAngle());
        }
    }

    private ScanResult mScanResult;

    private static int sServiceState;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.info("ServiceConnection onServiceConnected()");
            sServiceState = STATE_CONNECTED;
            mBleService = ((BleService.BleBinder) service).getService();

            mModel = new BluetoothModel();
            // Automatically connects to the device upon successful start-up initialization.
            mBleService.setListener(mListener);
            mBleService.setScanResult(mScanResult);
            mBleService.connect(mBleService.getScanResult());

            mActivity.setTitle(mScanResult.getDevice().getName());
            mActivity.showMainControlFragment();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.info("ServiceConnection onServiceDisconnected()");
            sServiceState = STATE_DISCONNECTED;
            mBleService = null;
        }
    };

    public MainPresenter(MainActivity activity) {
        this.mActivity = activity;
    }

    public Observable<ScanResult> scanForDevices() {
        return Observable.create(new ObservableOnSubscribe<ScanResult>() {

            @Override
            public void subscribe(final @NonNull ObservableEmitter<ScanResult> e) throws Exception {
                DataManager.getInstance().getBleManager().scanDevice(
                        new ListScanCallback(SCAN_TIME_OUT_MILLIS) {
                            @Override
                            public void onScanning(ScanResult result) {
                                e.onNext(result);
                            }

                            @Override
                            public void onScanComplete(ScanResult[] results) {
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void bindService(ScanResult scanResult) {
        mScanResult = scanResult;
        Intent intent = new Intent(mActivity, BleService.class);
        mActivity.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void onDestroy() {
        if (sServiceState == STATE_CONNECTED) {
            mActivity.unbindService(mServiceConnection);
            sServiceState = STATE_DISCONNECTED;
        }
    }

    public BluetoothModel getModel() {
        return mModel;
    }

    public BleService getBleService() {
        return mBleService;
    }
}
