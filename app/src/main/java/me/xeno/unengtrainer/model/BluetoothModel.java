package me.xeno.unengtrainer.model;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

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
import me.xeno.unengtrainer.util.Logger;
import me.xeno.unengtrainer.view.activity.BluetoothListActivity;

/**
 * Created by xeno on 2017/5/15.
 */

public class BluetoothModel {

    private static final int SCAN_TIME_OUT_MILLIS = 10000;

    public void sendInstrustion() {

    }

    public void getBattery() {

    }

    public void getMachineStatus(BluetoothGattCharacteristic characteristic) {
        //帧头（固定）0xFB，命令号(查询状态) 0x02，数据长度 0x00，数据（无），校验位 0xFD，帧尾（固定）0x0D
        characteristic.setValue(new byte[] {(byte)0xFB, 0x02, 0x00, (byte)0xFD,(byte) 0x0D});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    public void conductMachine(BluetoothGattCharacteristic characteristic) {
        //帧头（固定）0xFB，命令号（使能步进电机） 0x03，数据长度 0x00，数据（无），校验位 0xFE，帧尾（固定）0x0D
        characteristic.setValue(new byte[] {(byte)0xFB, 0x03, 0x00, (byte)0xFE,(byte) 0x0D});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    /**
     * @param characteristic
     * @param axis 第一轴：1 或 第二轴：2
     * @param switchOn 打开：true 或 关闭：false
     */
    public void switchMachineBrake(BluetoothGattCharacteristic characteristic, int axis, boolean switchOn) {
        //帧头（固定）0xFB，命令号（打开/关闭电机刹车） 0x04，数据长度 0x01，数据（Brake），校验位 0xFE，帧尾（固定）0x0D
        byte brake = 0x0;

        if(axis == 1)
        characteristic.setValue(new byte[] {(byte)0xFB, 0x04, 0x01, 0x00,0x00,(byte) 0xaa});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
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

    public Observable<ConnectionWrapper> connect(final Context context, final ScanResult scanResultToConnect, final boolean autoConnect) {
        return Observable.create(new ObservableOnSubscribe<ConnectionWrapper>() {
            @Override
            public void subscribe(final @NonNull ObservableEmitter<ConnectionWrapper> e) throws Exception {
                DataManager.getInstance().getBleManager().connectDevice(scanResultToConnect, autoConnect,
                        new BleGattCallback() {
                            @Override
                            public void onNotFoundDevice() {
                                Logger.info("onNotFoundDevice()");
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_DEVICE_NOT_FOUND);
                                wrapper.setMessage("xxxxxx: device not found, fail to connect.");
                                e.onNext(wrapper);
                            }

                            @Override
                            public void onFoundDevice(ScanResult scanResult) {
                                Logger.info("onFoundDevice()");
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_DEVICE_FOUND);
                                wrapper.setMessage("oooooo: device found! connecting...");
                                wrapper.setFoundDeviceResult(scanResult);
                                e.onNext(wrapper);
                            }

                            @Override
                            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                                Logger.info("onConnectSuccess()");

                                //根据demo源码,在此处回调调用
                                //根据网文：小米手机 在gatt 连接上时，在gatt.discoverServices 之前，加上 sleep(500) ，发现断开后在连接 成功率大大提高。
                                gatt.discoverServices();
                                //调用discoverServices()后，如果发现service成功，会进入onServicesDiscovered()回调

                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_CONNECT_SUCCESS);
                                wrapper.setMessage("oooooo: connect success! status = " + status);
                                wrapper.setBluetoothGatt(gatt);
                                wrapper.setStatus(status);
                                e.onNext(wrapper);
                            }

                            @Override
                            public void onConnectFailure(BleException exception) {
                                Logger.info("onConnectFailure()");
                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_CONNECT_FAIL);
                                wrapper.setMessage("xxxxxx: connect fail, code = " + exception.getCode() + " ,description -> " + exception.getDescription());
                                e.onNext(wrapper);
                                e.onComplete();
                            }

                            @Override
                            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                                super.onConnectionStateChange(gatt, status, newState);
                                Logger.info("onConnectionStateChange()");
                            }

                            @Override
                            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                                super.onServicesDiscovered(gatt, status);
                                Logger.info("onServicesDiscovered()调用" );

                                for(BluetoothGattService service : gatt.getServices()) {
                                    Logger.info("service: " + service.getUuid().toString());
                                    for(BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                                        Logger.info("characteristic: " + characteristic.getUuid().toString());
                                    }
                                }


                                //TODO 使用ServiceListFragment列出发现的service
                                ((BluetoothListActivity)context).showServiceRecyclerFragment();


                                ConnectionWrapper wrapper = new ConnectionWrapper();
                                wrapper.setMsgCode(ConnectionWrapper.MSG_CODE_SERVICES_DISCOVERD);
                                wrapper.setMessage("oooooo: services discovered! status = " + status);
                                wrapper.setBluetoothGatt(gatt);
                                wrapper.setStatus(status);
                                e.onNext(wrapper);

                            }

                            @Override
                            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                                super.onCharacteristicRead(gatt, characteristic, status);
                                Logger.info("onCharacteristicRead()");

                                //TODO 读取机器传输的字节 byte[]，接下来按照文档进行处理
                                characteristic.getValue();
                            }

                            @Override
                            /**
                             * Write characteristic, requesting acknoledgement by the remote device
                             */
//                                public static final int WRITE_TYPE_DEFAULT = 0x02;
                            /**
                             * Wrtite characteristic without requiring a response by the remote device
                             */
//                                public static final int WRITE_TYPE_NO_RESPONSE = 0x01;
                            /**
                             * Write characteristic including authentication signature
                             */
//                                public static final int WRITE_TYPE_SIGNED = 0x04;
                            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                                super.onCharacteristicWrite(gatt, characteristic, status);
                                Logger.info("onCharacteristicWrite()");

                                //TODO 向机器发送指令，需要封装成各种指令
                                getMachineStatus(characteristic);
                                //开始写数据
                                gatt.writeCharacteristic(characteristic);
                            }

                            @Override
                            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                                super.onCharacteristicChanged(gatt, characteristic);
                                Logger.info("onCharacteristicChanged()");
                            }

                            @Override
                            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                                super.onDescriptorRead(gatt, descriptor, status);
                                Logger.info("onDescriptorRead()");
                            }

                            @Override
                            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                                super.onDescriptorWrite(gatt, descriptor, status);
                                Logger.info("onDescriptorWrite()");
                            }

                            @Override
                            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                                super.onReliableWriteCompleted(gatt, status);
                                Logger.info("onReliableWriteCompleted()");
                            }

                            @Override
                            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                                super.onReadRemoteRssi(gatt, rssi, status);
                                Logger.info("onReadRemoteRssi()");
                            }

                            @Override
                            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                                super.onMtuChanged(gatt, mtu, status);
                                Logger.info("onMtuChanged()");
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
