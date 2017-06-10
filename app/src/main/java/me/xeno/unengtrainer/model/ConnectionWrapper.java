package me.xeno.unengtrainer.model;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.ScanResult;

/**
 * Created by xeno on 2017/5/25.
 */

public class ConnectionWrapper {

    public static final int MSG_CODE_DEVICE_NOT_FOUND = 510;
    public static final int MSG_CODE_DEVICE_FOUND = 210;
    public static final int MSG_CODE_CONNECT_SUCCESS = 211;
    public static final int MSG_CODE_CONNECT_FAIL = 511;


    private int msgCode;
    private String message;

    private ScanResult foundDeviceResult;

    private BluetoothGatt bluetoothGatt;
    private int status;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScanResult getFoundDeviceResult() {
        return foundDeviceResult;
    }

    public void setFoundDeviceResult(ScanResult foundDeviceResult) {
        this.foundDeviceResult = foundDeviceResult;
    }

    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
