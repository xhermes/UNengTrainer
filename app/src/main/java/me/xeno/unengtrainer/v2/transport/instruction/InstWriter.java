package me.xeno.unengtrainer.v2.transport.instruction;

import android.bluetooth.BluetoothGattCharacteristic;

import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.v2.transport.frame.FrameWriter;

public class InstWriter {

    public static byte[] getMachineStatus() {
        return FrameWriter.createFrame(Config.DATA_TYPE_GET_STATUS, new byte[]{});
    }

    public static void conductMachine(BluetoothGattCharacteristic characteristic) {
        //帧头（固定）0xFB，命令号（使能步进电机） 0x03，数据长度 0x00，数据（无），校验位 0xFE，帧尾（固定）0x0D
        characteristic.setValue(new byte[]{(byte) 0xFB, 0x03, 0x00, (byte) 0xFE, (byte) 0x0D});
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }

    /**
     * @param characteristic
     * @param axis           第一轴：1 或 第二轴：2
     * @param switchOn       打开：true 或 关闭：false
     */
    public static void switchMachineBrake(BluetoothGattCharacteristic characteristic, int axis, boolean switchOn) {
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
    public static byte[] setAxisAngle(double angle1, double angle2) {

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(angle1));
        sb.append(",");
        sb.append(String.valueOf(angle2));

        String strFrameData = sb.toString();

        return FrameWriter.createFrame(Config.DATA_TYPE_SET_AXIS_ANGLE, strFrameData.getBytes());
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
    public static byte[] runAxis(int axis1, int axis2) {
        byte[] frameData = new byte[]{(byte) axis1, (byte) axis2};
        return FrameWriter.createFrame(Config.DATA_TYPE_RUN_AXIS, frameData);
    }

    /**
     * 同意机器开始校准零位
     * 同意机器开始校准零位（开启对话框）->机器开始校准零位->完成校准回调onStatus->对话框销毁
     *
     * @return
     */
    public static byte[] makeZero() {
        return FrameWriter.createFrame(Config.DATA_TYPE_MAKE_ZERO, null);
    }

    public static byte[] setMotorSpeed(float motor1, float motor2) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(motor1));
        sb.append(",");
        sb.append(String.valueOf(motor2));
        String strFrameData = sb.toString();
        return FrameWriter.createFrame(Config.DATA_TYPE_SET_MOTOR_SPEED, strFrameData.getBytes());
    }

    public static byte[] getAxisAngle() {
        return FrameWriter.createFrame(Config.DATA_TYPE_GET_AXIS_ANGLE, null);
    }

    public static byte[] getBatteryVoltage() {
        return FrameWriter.createFrame(Config.DATA_TYPE_GET_BATTERY_VOLTAGE, null);
    }

    public static byte[] getMotorSpeed() {
        return FrameWriter.createFrame(Config.DATA_TYPE_GET_MOTOR_SPEED, null);
    }
}
