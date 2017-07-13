package me.xeno.unengtrainer.model;

import android.bluetooth.BluetoothGattCharacteristic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.DialogUtils;
import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2017/5/15.
 */

public class BluetoothModel {

    private static final byte FRAME_HEADER = (byte) 0xFB;
    private static final byte FRAME_END = (byte) 0x0D;

    public void sendInstrustion() {

    }

    public void getBattery() {

    }

    public void getMachineStatus(BluetoothGattCharacteristic characteristic) {
        //帧头（固定）0xFB，命令号(查询状态) 0x02，数据长度 0x00，数据（无），校验位 0xFD，帧尾（固定）0x0D
        characteristic.setValue(new byte[]{(byte) 0xFB, 0x02, 0x00, (byte) 0xFD, (byte) 0x0D});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    public void conductMachine(BluetoothGattCharacteristic characteristic) {
        //帧头（固定）0xFB，命令号（使能步进电机） 0x03，数据长度 0x00，数据（无），校验位 0xFE，帧尾（固定）0x0D
        characteristic.setValue(new byte[]{(byte) 0xFB, 0x03, 0x00, (byte) 0xFE, (byte) 0x0D});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    /**
     * @param characteristic
     * @param axis           第一轴：1 或 第二轴：2
     * @param switchOn       打开：true 或 关闭：false
     */
    public void switchMachineBrake(BluetoothGattCharacteristic characteristic, int axis, boolean switchOn) {
        //帧头（固定）0xFB，命令号（打开/关闭电机刹车） 0x04，数据长度 0x01，数据（Brake），校验位 0xFE，帧尾（固定）0x0D
        byte brake = 0x0;

        if (axis == 1)
            characteristic.setValue(new byte[]{(byte) 0xFB, 0x04, 0x01, 0x00, 0x00, (byte) 0xaa});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    /**
     * @param angle1 设置的角度，第一轴
     * @param angle2 设置的角度，第二轴
     * @return 将会开始写的数据
     */
    public byte[] setAxisAngle(double angle1, double angle2) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(angle1));
        sb.append(",");
        sb.append(String.valueOf(angle2));

        String strFrameData = sb.toString();

        return createFrame(Config.DATA_TYPE_SET_AXIS_ANGLE, strFrameData.getBytes());
    }

//    public byte[] setAxisSpeed() {
//        return createFrame(Config.DATA_TYPE_SET_AXIS_SPEED, )
//    }

    /**
     * 第一轴的运行/停止
     * 0:第一轴停止运行
     * 1:第一轴正方向连续运行(直到正限位或者报警)
     * 2:第一轴负方向连续运行(直到负限位或者报警)
     *
     * @param axis1 第一轴
     * @param axis2 第二轴
     * @return
     */
    public byte[] runAxis(int axis1, int axis2) {
        byte[] frameData = new byte[]{(byte) axis1, (byte) axis2};
        return createFrame(Config.DATA_TYPE_RUN_AXIS, frameData);
    }

    /**
     * 归零
     *
     * @return
     */
    public byte[] makeZero() {
        return createFrame(Config.DATA_TYPE_MAKE_ZERO, null);
    }

    public byte[] setMotorSpeed(int motor1, int motor2) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(motor1));
        sb.append(",");
        sb.append(String.valueOf(motor2));
        String strFrameData = sb.toString();
        return createFrame(Config.DATA_TYPE_SET_MOTOR_SPEED, strFrameData.getBytes());
    }

    public byte[] getAxisAngle() {
        return createFrame(Config.DATA_TYPE_GET_AXIS_ANGLE, null);
    }

    public byte[] getBatteryVoltage() {
        return createFrame(Config.DATA_TYPE_GET_BATTERY_VOLTAGE, null);
    }

    /**
     * @param type 命令号
     * @param data 写入数据，没有传null
     * @return 构建好的数据帧
     */
    public byte[] createFrame(byte type, byte[] data) {
        ArrayList<Byte> dataFrame = new ArrayList<>();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        //header
        outputStream.write(FRAME_HEADER);

        //type命令号
        outputStream.write(type);

        //长度，data为null时为0
        int length = 0;
        if (data != null) {
            length = data.length;
        }
        outputStream.write(length);

        //加入数据
        if (data != null) {
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //校验位 帧头+Type+Length+Data
        int crc = FRAME_HEADER + type + length;
        if(data != null) {
            for (byte b : data) {
                crc += b;
            }
        }
        Logger.info("发送：crc = " + crc);
        //TODO 超过255忽略溢出部分，还需要考虑-127,255问题，不知道会不会有问题
        if (crc > 255) {
            crc = crc & 0xFF;
        }
        outputStream.write(crc);

        //帧尾
        outputStream.write(FRAME_END);

        byte[] result = outputStream.toByteArray();
//        byte[] result = new byte[]{FRAME_HEADER, type, length, FRAME_END};
        Logger.info(CommonUtils.bytes2HexString(result));

        return result;
    }

}
