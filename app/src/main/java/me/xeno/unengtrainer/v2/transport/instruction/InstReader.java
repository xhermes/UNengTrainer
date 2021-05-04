package me.xeno.unengtrainer.v2.transport.instruction;

import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.util.Logger;

public class InstReader {


    public static void read(int type, byte[] finalData, BleServiceListener listener) {
        switch (type) {
            case Config.DATA_TYPE_GET_STATUS:
                listener.onGetStatus(InstHandler.handleGetStatus(finalData));
                break;
            case Config.DATA_TYPE_ENABLE:
                listener.onEnable(InstHandler.handleEnable(finalData));
                break;
            case Config.DATA_TYPE_SWITCH_BRAKE:
                listener.onTurnBrake(InstHandler.handleTurnBrake(finalData));
                break;
            case Config.DATA_TYPE_MAKE_ZERO:
                listener.onRequestMakeZero(InstHandler.handleRequestMakeZero(finalData));
                break;
            case Config.DATA_TYPE_SET_AXIS_ANGLE:
                listener.onSetAxisAngle(InstHandler.handleSetAxisAngle(finalData));
                break;
            case Config.DATA_TYPE_RUN_AXIS:
                listener.onRunAxis(InstHandler.handleRunAxis(finalData));
                break;
            case Config.DATA_TYPE_SET_AXIS_SPEED:
                listener.onSetAxisSpeed(InstHandler.handleSetAxisSpeed(finalData));
                break;
            case Config.DATA_TYPE_GET_AXIS_ANGLE:
                if (finalData != null) {
                    listener.onGetAxisAngle(InstHandler.handleGetAxisAngle(finalData));
                } else {
                    Logger.info("数据包长度为0");
                }
                break;
            case Config.DATA_TYPE_SET_MOTOR_SPEED:
                listener.onSetMotorSpeed(InstHandler.handleSetMotorSpeed(finalData));
                break;
            case Config.DATA_TYPE_GET_BATTERY_VOLTAGE:
                listener.onGetBatteryVoltage(InstHandler.handleGetBatteryVoltage(finalData));
                break;
            case Config.DATA_TYPE_GET_MOTOR_SPEED:
                listener.onGetMotorSpeed(InstHandler.handleGetMotorSpeed(finalData));
                break;
        }
    }
}
