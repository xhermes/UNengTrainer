//package me.xeno.unengtrainer.v2.ble;
//
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothManager;
//import android.content.Context;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//
//import com.clj.fastble.bluetooth.BleBluetooth;
//import com.clj.fastble.bluetooth.SplitWriter;
//import com.clj.fastble.callback.BleGattCallback;
//import com.clj.fastble.callback.BleWriteCallback;
//import com.clj.fastble.data.BleDevice;
//import com.clj.fastble.data.BleMsg;
//import com.clj.fastble.exception.BleException;
//import com.clj.fastble.exception.OtherException;
//import com.clj.fastble.utils.BleLog;
//
//import me.xeno.unengtrainer.application.DataManager;
//import me.xeno.unengtrainer.util.Logger;
//
//import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
//
//
//public class BleManager {
//
//    private static final String TAG = "BleManager";
//
//    public BluetoothGatt bluetoothGatt;
//    private BluetoothManager bluetoothManager;
//    private BleBluetooth bleBluetooth;
//    private Context context;
//    private Handler mainHandler = new Handler(Looper.getMainLooper());
//
//    public BleManager(Context context) {
//        this.context = context;
//
//        if (DataManager.getInstance().isSupportBle()) {
//            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//        }
//    }
//
//
//
//    public BluetoothGatt connect(BleDevice bleDevice, BleGattCallback bleGattCallback) {
//        if (bleGattCallback == null) {
//            throw new IllegalArgumentException("BleGattCallback can not be Null!");
//        }
//
//        if (!isBlueEnable()) {
//            Logger.error(TAG, "Bluetooth not enable!");
//            bleGattCallback.onConnectFail(bleDevice, new OtherException("Bluetooth not enable!"));
//            return null;
//        }
//
//        if (Looper.myLooper() == null || Looper.myLooper() != Looper.getMainLooper()) {
//            Logger.warning(TAG, "Be careful: currentThread is not MainThread!");
//        }
//
//        if (bleDevice == null || bleDevice.getDevice() == null) {
//            bleGattCallback.onConnectFail(bleDevice, new OtherException("Not Found Device Exception Occurred!"));
//        } else {
////            BleBluetooth bleBluetooth = multipleBluetoothController.buildConnectingBle(bleDevice);
//            bleBluetooth = new BleBluetooth(bleDevice);
////            boolean autoConnect = bleScanRuleConfig.isAutoConnect();
//
//            return connect(bleDevice, true, bleGattCallback);
//        }
//
//        return null;
//    }
//
//    public synchronized BluetoothGatt connect(BleDevice bleDevice,
//                                              boolean autoConnect,
//                                              BleGattCallback callback) {
//        BleLog.i("connect device: " + bleDevice.getName()
//                + "\nmac: " + bleDevice.getMac()
//                + "\nautoConnect: " + autoConnect
//                + "\ncurrentThread: " + Thread.currentThread().getId());
//
////        addConnectGattCallback(callback);
//
////        lastState = BleBluetooth.LastState.CONNECT_CONNECTING;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            bluetoothGatt = bleDevice.getDevice().connectGatt(context,
//                    autoConnect, callback, TRANSPORT_LE);
//        } else {
//            bluetoothGatt = bleDevice.getDevice().connectGatt(context,
//                    autoConnect, callback);
//        }
//        if (bluetoothGatt != null) {
//            if (callback != null) {
//                callback.onStartConnect();
//            }
//            Message message = mainHandler.obtainMessage();
//            message.what = BleMsg.MSG_CONNECT_OVER_TIME;
//            mainHandler.sendMessageDelayed(message, com.clj.fastble.BleManager.getInstance().getConnectOverTime());
//
//        } else {
////            disconnectGatt();
////            refreshDeviceCache();
////            closeBluetoothGatt();
////            lastState = BleBluetooth.LastState.CONNECT_FAILURE;
////            com.clj.fastble.BleManager.getInstance().getMultipleBluetoothController().removeConnectingBle(BleBluetooth.this);
//            if (callback != null)
//                callback.onConnectFail(bleDevice, new OtherException("GATT connect exception occurred!"));
//
//        }
//        return bluetoothGatt;
//    }
//
//    /**
//     * Open bluetooth
//     */
//    public void enableBluetooth() {
//        if (DataManager.getInstance().getBluetoothAdapter() != null) {
//            DataManager.getInstance().getBluetoothAdapter().enable();
//        }
//    }
//
//    /**
//     * Disable bluetooth
//     */
//    public void disableBluetooth() {
//        if (DataManager.getInstance().getBluetoothAdapter() != null) {
//            if (DataManager.getInstance().getBluetoothAdapter().isEnabled())
//                DataManager.getInstance().getBluetoothAdapter().disable();
//        }
//    }
//
//    public boolean isBlueEnable() {
//        return DataManager.getInstance().getBluetoothAdapter() != null && DataManager.getInstance().getBluetoothAdapter().isEnabled();
//    }
//
//    public void write(BleDevice bleDevice,
//                      String uuid_service,
//                      String uuid_write,
//                      byte[] data,
//                      boolean split,
//                      BleWriteCallback callback) {
//
//        if (callback == null) {
//            throw new IllegalArgumentException("BleWriteCallback can not be Null!");
//        }
//
//        if (data == null) {
//            BleLog.e("data is Null!");
//            callback.onWriteFailure(new OtherException("data is Null!"));
//            return;
//        }
//
//        if (data.length > 20 && !split) {
//            BleLog.w("Be careful: data's length beyond 20! Ensure MTU higher than 23, or use spilt write!");
//        }
//
//        if (bleBluetooth == null) {
//            callback.onWriteFailure(new OtherException("This device not connect!"));
//        } else {
//            if (split && data.length > 20) {
//                new SplitWriter().splitWrite(bleBluetooth, uuid_service, uuid_write, data, callback);
//            } else {
//                bleBluetooth.newBleConnector()
//                        .withUUIDString(uuid_service, uuid_write)
//                        .writeCharacteristic(data, callback, uuid_write);
//            }
//        }
//    }
//}
