package me.xeno.unengtrainer.service;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.xeno.unengtrainer.application.BleSppGattAttributes;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.util.Logger;

import static me.xeno.unengtrainer.application.Config.STATE_CONNECTED;
import static me.xeno.unengtrainer.application.Config.STATE_DISCONNECTED;

/**
 * Created by Administrator on 2017/6/11.
 */

public class BleService extends Service {

    private static final boolean AUTO_CONNECT = false;

    private BleServiceListener mListener;//TODO 记得从MainActivity传入值

    BleServiceProcessor mProcessor;

    private BleBinder mBinder = new BleBinder();

    private BluetoothGatt mBluetoothGatt;

    private ScanResult mScanResult;

    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private int mConnectionState = STATE_DISCONNECTED;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.info("BleService onCreate()");
        mProcessor = new BleServiceProcessor();
    }

    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onNotFoundDevice() {
            Logger.info("onNotFoundDevice()");
        }

        @Override
        public void onFoundDevice(ScanResult scanResult) {
            Logger.info("onFoundDevice()");
        }

        @Override
        public void onConnectSuccess(BluetoothGatt gatt, int status) {
            Logger.info("onConnectSuccess() status = " + status);

            //根据demo源码,在此处回调调用
            //根据网文：小米手机 在gatt 连接上时，在gatt.discoverServices 之前，加上 sleep(500) ，发现断开后在连接 成功率大大提高。
            gatt.discoverServices();
            //调用discoverServices()后，如果发现service成功，会进入onServicesDiscovered()回调
        }

        @Override
        public void onConnectFailure(BleException exception) {
            //FIXME onConnectFailure() exception = ConnectException{gattStatus=133, bluetoothGatt=android.bluetooth.BluetoothGatt@4bcb88d} BleException { code=201, description='Gatt Exception Occurred! '}
            //FIXME 有时候点击连接设备，已经进入了maincontrol但是会出现以上错误，此时蓝牙连接失败，然而所有按钮都有响应，于是会引起未知错误。
            Logger.info("onConnectFailure() exception = " + exception.toString());
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Logger.info("onConnectionStateChange()");

            //2017.9.16 如果检测到机器已断开蓝牙连接（比如机器已关机）
            if(newState == BluetoothProfile.STATE_DISCONNECTED) {
               disconnect();
                mListener.onDisconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Logger.info("onServicesDiscovered() status = " + status);

            mBluetoothGatt = gatt;

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 默认先使用 B-0006/TL8266 服务发现
                BluetoothGattService service = gatt.getService(UUID.fromString(BleSppGattAttributes.BLE_SPP_Service));
                if (service!=null) {
                    //找到服务，继续查找特征值
                    mNotifyCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
                    mWriteCharacteristic  = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Write_Characteristic));

                    if (mNotifyCharacteristic!=null) {
                        //TODO Demo中在此处向activity发送了标记连接成功的广播，并进行了UI更新。即将执行到此处定义为连接成功
                        mConnectionState = STATE_CONNECTED;

                        // 使能Notify，开始接收characteristicChange的消息
                        setCharacteristicNotification(mNotifyCharacteristic, true);
                    }
                } else {
                    Logger.warning("onServicesDiscovered(): service is NULL");
                }
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Logger.info("onCharacteristicRead()");

            //主动读取，不适用
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.info("onCharacteristicRead(): " + bytes2HexString(characteristic.getValue()));
                handleReceivedData(characteristic.getValue());
            }
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
            Logger.debug("onCharacteristicWrite()");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                //TODO 此方法只作为发送写指令成功时的回调，Log成功就可以了
                Logger.debug("onCharacteristicWrite(): write SUCCESS");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Logger.debug("onCharacteristicChanged(): value = " + bytes2HexString(characteristic.getValue()));

            //监听，这里还在非主线程
            handleReceivedData(characteristic.getValue());
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


    };

    BleCharacterCallback mBleCharacterCallback = new BleCharacterCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            Logger.debug("BleCharacterCallback onSuccess() ");
        }

        @Override
        public void onFailure(BleException exception) {
            //FIXME code=301, description='this characteristic not support write!
            Logger.error("BleCharacterCallback onFailure() exception: " + exception.toString());
            //TODO 每次发送命令如果机器没有响应，这里会抓到错误，考虑要不要在这里加回调
        }
    };

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if(!DataManager.getInstance().getBleManager().isBlueEnable()) {
            Logger.warning("setCharacteristicNotification(): bluetooth is NOT activate");
            return ;
        }
        if(mBluetoothGatt == null) {
            Logger.warning("setCharacteristicNotification(): mBluetoothGatt is NULL");
            return ;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to BLE SPP Notify.
        if (UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic).equals(characteristic.getUuid())) {
            //TODO 这里是干什么？
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(BleSppGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final ScanResult scanResult) {
        if(!DataManager.getInstance().getBleManager().isBlueEnable()) {
            Logger.warning("connect(): bluetooth is NOT activate");
            return false;
        }
        if(scanResult == null) {
            Logger.warning("connect(): scanResult is NULL");
        }

        DataManager.getInstance().getBleManager().connectDevice(scanResult, AUTO_CONNECT, mBleGattCallback);
//        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     * 复位（断开此次蓝牙连接，移除所有回调）
     */
    public void disconnect() {
        if(!DataManager.getInstance().getBleManager().isBlueEnable()) {
            Logger.warning("disconnect(): bluetooth is NOT activate");
        }
        DataManager.getInstance().getBleManager().closeBluetoothGatt();
    }

    public void writeData(byte[] data) {
        if(!DataManager.getInstance().getBleManager().isBlueEnable()) {
            Logger.warning("writeData(): bluetooth is NOT activate");
        }

        //TODO java.lang.NullPointerException: Attempt to invoke virtual method 'java.util.UUID android.bluetooth.BluetoothGattCharacteristic.getUuid()' on a null object reference
        if(mConnectionState == STATE_CONNECTED) {
            DataManager.getInstance().getBleManager().writeDevice(BleSppGattAttributes.BLE_SPP_Service,
                    mWriteCharacteristic.getUuid().toString(), data, mBleCharacterCallback);
        } else {
            Logger.error("write data: service is not ready, please wait..");
        }

    }

    public void handleReceivedData(byte[] dataPacket) {
        //handle all data from bluetooth

        byte header = dataPacket[0];
        //FIXME java.lang.ArrayIndexOutOfBoundsException: length=1; index=1
        final byte type = dataPacket[1];
        byte length = dataPacket[2];
        byte[] data = null;
        if(length > 0){
            data = new byte[length];
            for(int i=3;i<3+length;i++){
                data[i-3] = dataPacket[i];
            }
        }
        int crc = dataPacket[dataPacket.length - 2];
        if(crc<0)
            crc+=256;
        byte end = dataPacket[dataPacket.length - 1];

        //回复帧头固定为0xFC，若非直接抛弃
        //TODO byte最大只有128，当然不可能是0xFC
//        if(header != 0xFC) {
//            Logger.warning("接收到蓝牙数据，帧头数据错误！");
//            return;
//        }

        // 使用校验位，校验数据，失败直接抛弃
        int crcCalculate;
        crcCalculate = header + type + length;
        if(data != null) {
            for (Byte b : data) {
                crcCalculate+=b;
            }
        }
        Logger.debug("接收：crcCalculate = " + crcCalculate);

        if(crcCalculate > 255) {
            crcCalculate = crcCalculate & 0xFF;
        }

        if(crc != crcCalculate) {
            Logger.warning("接收到蓝牙数据，校验错误！crc=" + crc + " ,crcCalculate=" + crcCalculate);
            return;
        }

        //打印数据长度
        Logger.debug("handleReceivedData: data Length = " + length);

        //根据命令号处理，需要切换到主线程
        final byte[] finalData = data;

        //如果data为null，直接放弃处理
        if(finalData != null) {
            Observable.just(new Object())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {

                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            switch (type) {
                                case Config.DATA_TYPE_GET_STATUS:
                                    mListener.onGetStatus(mProcessor.handleGetStatus(finalData));
                                    break;
                                case Config.DATA_TYPE_ENABLE:
                                    mListener.onEnable(mProcessor.handleEnable(finalData));
                                    break;
                                case Config.DATA_TYPE_SWITCH_BRAKE:
                                    mListener.onTurnBrake(mProcessor.handleTurnBrake(finalData));
                                    break;
                                case Config.DATA_TYPE_MAKE_ZERO:
                                    mListener.onMakeZeroCompleted(mProcessor.handleMakeZeroCompleted(finalData));
                                    break;
                                case Config.DATA_TYPE_SET_AXIS_ANGLE:
                                    mListener.onSetAxisAngle(mProcessor.handleSetAxisAngle(finalData));
                                    break;
                                case Config.DATA_TYPE_RUN_AXIS:
                                    mListener.onRunAxis(mProcessor.handleRunAxis(finalData));
                                    break;
                                case Config.DATA_TYPE_SET_AXIS_SPEED:
                                    mListener.onSetAxisSpeed(mProcessor.handleSetAxisSpeed(finalData));
                                    break;
                                case Config.DATA_TYPE_GET_AXIS_ANGLE:
                                    if (finalData != null) {
                                        mListener.onGetAxisAngle(mProcessor.handleGetAxisAngle(finalData));
                                    } else {
                                        Logger.info("数据包长度为0");
                                    }
                                    break;
                                case Config.DATA_TYPE_SET_MOTOR_SPEED:
                                    mListener.onSetMotorSpeed(mProcessor.handleSetMotorSpeed(finalData));
                                    break;
                                case Config.DATA_TYPE_GET_BATTERY_VOLTAGE:
                                    mListener.onGetBatteryVoltage(mProcessor.handleGetBatteryVoltage(finalData));
                                    break;
                            }
                        }
                    });
        }
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.info("BleService onBind()");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disconnect();
        return super.onUnbind(intent);
    }

    public class BleBinder extends Binder {
        public BleService getService() {
            return BleService.this;
        }
    }

    public ScanResult getScanResult() {
        return mScanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.mScanResult = scanResult;
    }

    public void setListener(BleServiceListener listener) {
        this.mListener = listener;
    }
}
