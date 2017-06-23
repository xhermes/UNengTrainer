package me.xeno.unengtrainer.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.clj.fastble.data.ScanResult;
import com.clj.fastble.scan.ListScanCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.model.BluetoothModel;
import me.xeno.unengtrainer.service.BleService;
import me.xeno.unengtrainer.util.Logger;
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
        public void onReceiveData(byte[] data) {
            mModel.handleReceivedData();
        }
    };

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
        if(sServiceState == STATE_CONNECTED) {
            mActivity.unbindService(mServiceConnection);
            sServiceState = STATE_DISCONNECTED;
        }
    }

    public BluetoothModel getModel() {
        return mModel;
    }
}
