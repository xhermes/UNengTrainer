package me.xeno.unengtrainer.model;

import com.clj.fastble.data.ScanResult;
import com.clj.fastble.scan.ListScanCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2017/5/15.
 */

public class BluetoothModel {

    private static final int SCAN_TIME_OUT_MILLIS = 10000;

    public void sendInstrustion() {

    }

    public void getBattery() {

    }

    public Observable<BleDevice> scanForDevices() {
        return Observable.create(new ObservableOnSubscribe<BleDevice>() {

            @Override
            public void subscribe(final @NonNull ObservableEmitter<BleDevice> e) throws Exception {
                DataManager.getInstance().getBleManager().scanDevice(
                        new ListScanCallback(SCAN_TIME_OUT_MILLIS) {
                            @Override
                            public void onScanning(ScanResult result) {
                                BleDevice device = new BleDevice();
                                device.setName(result.getDevice().getName());
                                device.setAddress(result.getDevice().getAddress());
                                e.onNext(device);
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

}
