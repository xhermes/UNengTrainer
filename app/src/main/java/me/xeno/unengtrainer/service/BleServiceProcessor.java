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
import me.xeno.unengtrainer.util.Logger;

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

        EnableWrapper wrapper = new EnableWrapper();

        if(data == null) {
            //返回成功时，data为空
            wrapper.setSuccess(true);
        } else {
            //返回失败时，data为ERR
            byte err = data[0];
            wrapper.setAxis1Alert(getBit0(err));
            wrapper.setAxis2Alert(getBit1(err));
        }

        return wrapper;
    }

    public TurnBrakeWrapper handleTurnBrake(byte[] data) {



        return new TurnBrakeWrapper();
    }

    public MakeZeroCompletedWrapper handleMakeZeroCompleted(byte[] data) {

        byte axis1 = data[0];
        byte axis2 = data[1];

        MakeZeroCompletedWrapper wrapper = new MakeZeroCompletedWrapper();

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

        wrapper.setAxisStatuses(axisStatuses);

        return wrapper;
    }

    public SetAxisAngleWrapper handleSetAxisAngle(byte[] data) {
        //收到数据，表示机器收到指令
        return new SetAxisAngleWrapper();
    }

    public RunAxisWrapper handleRunAxis(byte[] data) {
        return new RunAxisWrapper();
    }

    public SetAxisSpeedWrapper handleSetAxisSpeed(byte[] data) {
        return new SetAxisSpeedWrapper();
    }

    public GetAxisAngleWrapper handleGetAxisAngle(byte[] data) {

        GetAxisAngleWrapper wrapper = new GetAxisAngleWrapper();

        String dataStr = asciiToString(data);
        String[] angleStr = new String[2];

            angleStr = dataStr.split(",");

        if(angleStr[0] != null) {
            wrapper.setAxis1Angle(angleStr[0]);
        }
        if(angleStr[1] != null) {
            wrapper.setAxis2Angle(angleStr[1]);
        }

        return wrapper;
    }

    public SetMotorSpeedWrapper handleSetMotorSpeed(byte[] data) {
        return new SetMotorSpeedWrapper();
    }

    public GetBatteryVoltageWrapper handleGetBatteryVoltage(byte[] data) {

        GetBatteryVoltageWrapper wrapper = new GetBatteryVoltageWrapper();

        String dataStr = asciiToString(data);
        Logger.info("dataStr=============>" + dataStr);

        if(dataStr != null) {
            wrapper.setVoltage(dataStr);
        }

        return wrapper;
    }

    public String asciiToString(byte[] bytes) {
        //FIXME java.lang.NullPointerException: Attempt to get length of null array
        char[] buf = new char[bytes.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) bytes[i];
            sb.append(buf[i]);
        }
        return sb.toString();
    }

}
