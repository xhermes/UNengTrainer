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

/**
 * Created by xeno on 2017/5/15.
 */

public class BluetoothModel {

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

    public void handleReceivedData() {
        //TODO 处理接收到的所有蓝牙消息
    }



}
