package me.xeno.unengtrainer.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.clj.fastble.conn.BleCharacterCallback;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;

import java.io.FileDescriptor;
import java.util.UUID;

import me.xeno.unengtrainer.application.BleSppGattAttributes;
import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.model.ConnectionWrapper;
import me.xeno.unengtrainer.util.Logger;

/**
 * Created by Administrator on 2017/6/11.
 * //TODO 需要用此service来保存BLE连接成功后的状态，可以参考github fastble源码
 */

public class BleService extends Service {

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private static final boolean AUTO_CONNECT = false;

    private BleServiceListener mListener;//TODO 记得从MainActivity传入值

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
            Logger.info("onConnectFailure() exception = " + exception.toString());
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Logger.info("onConnectionStateChange()");
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Logger.info("onServicesDiscovered() status = " + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 默认先使用 B-0006/TL8266 服务发现
                BluetoothGattService service = gatt.getService(UUID.fromString(BleSppGattAttributes.BLE_SPP_Service));
                if (service!=null) {
                    //找到服务，继续查找特征值
                    mNotifyCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
                    mWriteCharacteristic  = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Write_Characteristic));

                    if (mNotifyCharacteristic!=null) {
                        //TODO Demo中在此处向activity发送了标记连接成功的广播，并进行了UI更新。即将执行到此处定义为连接成功

                        //TODO 使能Notify，这个操作有何作用？
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

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Logger.info("onCharacteristicRead(): " + bytes2HexString(characteristic.getValue()));
                mListener.onReceiveData(bytes2HexString(characteristic.getValue()));
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
            Logger.info("onCharacteristicWrite()");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                //TODO 此方法只作为发送写指令成功时的回调，Log成功就可以了
                Logger.info("onCharacteristicWrite(): write SUCCESS");
            }
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


    };

    BleCharacterCallback mBleCharacterCallback = new BleCharacterCallback() {
        @Override
        public void onSuccess(BluetoothGattCharacteristic characteristic) {
            Logger.info("BleCharacterCallback onSuccess() ");
        }

        @Override
        public void onFailure(BleException exception) {
            Logger.info("BleCharacterCallback onFailure() exception: " + exception.toString());
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
        mConnectionState = STATE_CONNECTING;
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

        DataManager.getInstance().getBleManager().writeDevice(BleSppGattAttributes.BLE_SPP_Service,
                mWriteCharacteristic.getUuid().toString(), data, mBleCharacterCallback);

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
}
