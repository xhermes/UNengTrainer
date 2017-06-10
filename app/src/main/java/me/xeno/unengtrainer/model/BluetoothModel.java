package me.xeno.unengtrainer.model;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.ListScanCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import me.xeno.unengtrainer.application.DataManager;

/**
 * Created by xeno on 2017/5/15.
 */

public class BluetoothModel {

    private static final int SCAN_TIME_OUT_MILLIS = 10000;

    public void sendInstrustion() {

    }

    public void getBattery() {

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

    public Observable<ConnectionWrapper> connect(final ScanResult scanResultToConnect, final boolean autoConnect) {
        return Observable.create(new ObservableOnSubscribe<ConnectionWrapper>() {
            @Override
            public void subscribe(final @NonNull ObservableEmitter<ConnectionWrapper> e) throws Exception {
                DataManager.getInstance().getBleManager().connectDevice(scanResultToConnect, autoConnect,
                        new BleGattCallback() {
                            @Override
                            public void onNotFoundDevice() {
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_DEVICE_NOT_FOUND);
                                wrapper.setMessage("xxxxxx: device not found, fail to connect.");
                                e.onNext(wrapper);
                            }

                            @Override
                            public void onFoundDevice(ScanResult scanResult) {
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_DEVICE_FOUND);
                                wrapper.setMessage("oooooo: device found! connecting...");
                                wrapper.setFoundDeviceResult(scanResult);
                                e.onNext(wrapper);
                            }

                            @Override
                            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_CONNECT_SUCCESS);
                                wrapper.setMessage("oooooo: connect success! status = " + status);
                                wrapper.setBluetoothGatt(gatt);
                                wrapper.setStatus(status);
                                e.onNext(wrapper);
                                e.onComplete();
                            }

                            @Override
                            public void onConnectFailure(BleException exception) {
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_CONNECT_FAIL);
                                wrapper.setMessage("xxxxxx: connect fail, code = " + exception.getCode() + " ,description -> " + exception.getDescription());
                                e.onNext(wrapper);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
