package me.xeno.unengtrainer.listener;

import me.xeno.unengtrainer.model.entity.EnableWrapper;
import me.xeno.unengtrainer.model.entity.GetAxisAngleWrapper;
import me.xeno.unengtrainer.model.entity.GetBatteryVoltageWrapper;
import me.xeno.unengtrainer.model.entity.GetStatusWrapper;
import me.xeno.unengtrainer.model.entity.MakeZeroCompletedWrapper;
import me.xeno.unengtrainer.model.entity.RunAxisWrapper;
import me.xeno.unengtrainer.model.entity.SetAxisAngleWrapper;
import me.xeno.unengtrainer.model.entity.SetAxisSpeedWrapper;
import me.xeno.unengtrainer.model.entity.SetMotorSpeedWrapper;
import me.xeno.unengtrainer.model.entity.TurnBrakeWrapper;

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
    void onMakeZeroCompleted(MakeZeroCompletedWrapper wrapper);
    void onSetAxisAngle(SetAxisAngleWrapper wrapper);
    void onRunAxis(RunAxisWrapper wrapper);
    void onSetAxisSpeed(SetAxisSpeedWrapper wrapper);
    void onGetAxisAngle(GetAxisAngleWrapper wrapper);
    void onSetMotorSpeed(SetMotorSpeedWrapper wrapper);
    void onGetBatteryVoltage(GetBatteryVoltageWrapper wrapper);

}
