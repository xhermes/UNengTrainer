package me.xeno.unengtrainer.service;

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
 * Created by Administrator on 2017/6/24.
 */

public class BleServiceProcessor {

    private int getBit7(byte data) {
        return (data & 0x01);
    }
    private int getBit6(byte data) {
        return ((data & 0x02) >> 1);
    }
    private int getBit5(byte data) {
        return ((data & 0x04) >> 2);
    }
    private int getBit4(byte data) {
        return ((data & 0x08) >> 3);
    }
    private int getBit3(byte data) {
        return ((data & 0x10) >> 4);
    }
    private int getBit2(byte data) {
        return ((data & 0x20) >> 5);
    }
    private int getBit1(byte data) {
        return ((data & 0x40) >> 6);
    }
    private int getBit0(byte data) {
        return ((data & 0x80) >> 7);
    }


    public GetStatusWrapper handleGetStatus(byte[] data) {

        byte axis1 = data[0];
        byte axis2 = data[1];

        GetStatusWrapper.AxisStatus[] axisStatuses = new GetStatusWrapper.AxisStatus[2];
        axisStatuses[0].setRunning(getBit0(axis1));
        axisStatuses[0].setAbruptStopping(getBit1(axis1));
        axisStatuses[0].setAlerting(getBit2(axis1));
        axisStatuses[0].setPositiveSpacing(getBit3(axis1));
        axisStatuses[0].setNegativeSpacing(getBit4(axis1));

        axisStatuses[1].setRunning(getBit0(axis2));
        axisStatuses[1].setAbruptStopping(getBit1(axis2));
        axisStatuses[1].setAlerting(getBit2(axis2));
        axisStatuses[1].setPositiveSpacing(getBit3(axis2));
        axisStatuses[1].setNegativeSpacing(getBit4(axis2));

        GetStatusWrapper wrapper = new GetStatusWrapper();
        wrapper.setAxisStatuses(axisStatuses);

        return wrapper;
    }

    public EnableWrapper handleEnable(byte[] data) {
        return new EnableWrapper();
    }

    public TurnBrakeWrapper handleTurnBrake(byte[] data) {
        return new TurnBrakeWrapper();
    }

    public MakeZeroCompletedWrapper handleMakeZeroCompleted(byte[] data) {
        return new MakeZeroCompletedWrapper();
    }

    public SetAxisAngleWrapper handleSetAxisAngle(byte[] data) {
        return new SetAxisAngleWrapper();
    }

    public RunAxisWrapper handleRunAxis(byte[] data) {
        return new RunAxisWrapper();
    }

    public SetAxisSpeedWrapper handleSetAxisSpeed(byte[] data) {
        return new SetAxisSpeedWrapper();
    }

    public GetAxisAngleWrapper handleGetAxisAngle(byte[] data) {
        return new GetAxisAngleWrapper();
    }

    public SetMotorSpeedWrapper handleSetMotorSpeed(byte[] data) {
        return new SetMotorSpeedWrapper();
    }

    public GetBatteryVoltageWrapper handleGetBatteryVoltage(byte[] data) {
        return new GetBatteryVoltageWrapper();
    }

}
