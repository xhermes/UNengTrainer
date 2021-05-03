package me.xeno.unengtrainer.listener;

import me.xeno.unengtrainer.transport.frame.data.EnableWrapper;
import me.xeno.unengtrainer.transport.frame.data.GetAxisAngleWrapper;
import me.xeno.unengtrainer.transport.frame.data.GetBatteryVoltageWrapper;
import me.xeno.unengtrainer.transport.frame.data.GetStatusWrapper;
import me.xeno.unengtrainer.transport.frame.data.MakeZeroCompletedWrapper;
import me.xeno.unengtrainer.transport.frame.data.RunAxisWrapper;
import me.xeno.unengtrainer.transport.frame.data.SetAxisAngleWrapper;
import me.xeno.unengtrainer.transport.frame.data.SetAxisSpeedWrapper;
import me.xeno.unengtrainer.transport.frame.data.SetMotorSpeedWrapper;
import me.xeno.unengtrainer.transport.frame.data.TurnBrakeWrapper;

/**
 * Created by xeno on 2017/5/17.
 */
public interface BleServiceListener {

    /**
     * get status
     * @param wrapper
     */
    void onGetStatus(GetStatusWrapper wrapper);
    void onEnable(EnableWrapper wrapper);
    void onTurnBrake(TurnBrakeWrapper wrapper);
    void onRequestMakeZero(MakeZeroCompletedWrapper wrapper);
    void onSetAxisAngle(SetAxisAngleWrapper wrapper);
    void onRunAxis(RunAxisWrapper wrapper);
    void onSetAxisSpeed(SetAxisSpeedWrapper wrapper);
    void onGetAxisAngle(GetAxisAngleWrapper wrapper);
    void onSetMotorSpeed(SetMotorSpeedWrapper wrapper);
    void onGetBatteryVoltage(GetBatteryVoltageWrapper wrapper);
    void onGetMotorSpeed(GetAxisAngleWrapper wrapper);

    /**
     * 检测到机器断开蓝牙连接
     */
    void onDisconnect();

    void onReadRemoteRssi(int rssi);

}
