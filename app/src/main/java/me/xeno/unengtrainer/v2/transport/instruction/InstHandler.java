package me.xeno.unengtrainer.v2.transport.instruction;

import me.xeno.unengtrainer.v2.transport.BluetoothHelper;
import me.xeno.unengtrainer.v2.transport.frame.data.EnableWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.GetAxisAngleWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.GetBatteryVoltageWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.GetStatusWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.MakeZeroCompletedWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.RunAxisWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.SetAxisAngleWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.SetAxisSpeedWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.SetMotorSpeedWrapper;
import me.xeno.unengtrainer.v2.transport.frame.data.TurnBrakeWrapper;
import me.xeno.unengtrainer.util.Logger;

/**
 * Created by Administrator on 2017/6/24.
 * 负责蓝牙底层的数据包解析
 */

public class InstHandler {

    private static int getBit7(byte data) {
        return (data & 0x01);
    }
    private static int getBit6(byte data) {
        return ((data & 0x40));
    }
    private static int getBit5(byte data) {
        return ((data & 0x04) >> 2);
    }
    private static int getBit4(byte data) {
        return ((data & 0x08) >> 3);
    }
    private static int getBit3(byte data) {
        return ((data & 0x10) >> 4);
    }
    private static int getBit2(byte data) {
        return ((data & 0x20) >> 5);
    }
    private static int getBit1(byte data) {
        return ((data & 0x40) >> 6);
    }
    private static int getBit0(byte data) {
        return ((data & 0x80) >> 7);
    }


    public static GetStatusWrapper handleGetStatus(byte[] data) {
//  TODO Caused by: java.lang.NullPointerException: Attempt to read from null array
        byte axis1 = data[0];
        byte axis2 = data[1];

        GetStatusWrapper.AxisStatus[] axisStatuses = new GetStatusWrapper.AxisStatus[2];
        axisStatuses[0] = new GetStatusWrapper.AxisStatus();
        axisStatuses[0].setRunning(getBit0(axis1));
        axisStatuses[0].setAbruptStopping(getBit1(axis1));
        axisStatuses[0].setAlerting(getBit2(axis1));
        axisStatuses[0].setPositiveSpacing(getBit3(axis1));
        axisStatuses[0].setNegativeSpacing(getBit4(axis1));

        axisStatuses[1] = new GetStatusWrapper.AxisStatus();
        axisStatuses[1].setRunning(getBit0(axis2));
        axisStatuses[1].setAbruptStopping(getBit1(axis2));
        axisStatuses[1].setAlerting(getBit2(axis2));
        axisStatuses[1].setPositiveSpacing(getBit3(axis2));
        axisStatuses[1].setNegativeSpacing(getBit4(axis2));

        GetStatusWrapper wrapper = new GetStatusWrapper();
        wrapper.setAxisStatuses(axisStatuses);
        //是否校准零位完成
        if(getBit6(axis1) > 0) {
            wrapper.setMakeZeroCompleted(true);
        } else {
            wrapper.setMakeZeroCompleted(false);
        }

        return wrapper;
    }

    public static EnableWrapper handleEnable(byte[] data) {

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

    public static TurnBrakeWrapper handleTurnBrake(byte[] data) {



        return new TurnBrakeWrapper();
    }

    public static MakeZeroCompletedWrapper handleRequestMakeZero(byte[] data) {

//        byte axis1 = data[0];
//        byte axis2 = data[1];
//
        MakeZeroCompletedWrapper wrapper = new MakeZeroCompletedWrapper();
//
//        GetStatusWrapper.AxisStatus[] axisStatuses = new GetStatusWrapper.AxisStatus[2];
//        axisStatuses[0].setRunning(getBit0(axis1));
//        axisStatuses[0].setAbruptStopping(getBit1(axis1));
//        axisStatuses[0].setAlerting(getBit2(axis1));
//        axisStatuses[0].setPositiveSpacing(getBit3(axis1));
//        axisStatuses[0].setNegativeSpacing(getBit4(axis1));
//
//        axisStatuses[1].setRunning(getBit0(axis2));
//        axisStatuses[1].setAbruptStopping(getBit1(axis2));
//        axisStatuses[1].setAlerting(getBit2(axis2));
//        axisStatuses[1].setPositiveSpacing(getBit3(axis2));
//        axisStatuses[1].setNegativeSpacing(getBit4(axis2));
//
//        wrapper.setAxisStatuses(axisStatuses);

        return wrapper;
    }

    public static SetAxisAngleWrapper handleSetAxisAngle(byte[] data) {
        //收到数据，表示机器收到指令
        return new SetAxisAngleWrapper();
    }

    public static RunAxisWrapper handleRunAxis(byte[] data) {
        return new RunAxisWrapper();
    }

    public static SetAxisSpeedWrapper handleSetAxisSpeed(byte[] data) {
        return new SetAxisSpeedWrapper();
    }

    public static GetAxisAngleWrapper handleGetAxisAngle(byte[] data) {

        GetAxisAngleWrapper wrapper = new GetAxisAngleWrapper();

        String dataStr = BluetoothHelper.asciiToString(data);
        String[] angleStr;

            angleStr = dataStr.split(",");

        if(angleStr[0] != null) {
            wrapper.setAxis1Angle(angleStr[0]);
        }
        if(angleStr[1] != null) {
            wrapper.setAxis2Angle(angleStr[1]);
        }

        return wrapper;
    }

    public static SetMotorSpeedWrapper handleSetMotorSpeed(byte[] data) {
        return new SetMotorSpeedWrapper();
    }

    public static GetBatteryVoltageWrapper handleGetBatteryVoltage(byte[] data) {

        GetBatteryVoltageWrapper wrapper = new GetBatteryVoltageWrapper();

        String dataStr = BluetoothHelper.asciiToString(data);
        Logger.info("dataStr=============>" + dataStr);

        if(dataStr != null) {
            wrapper.setVoltage(dataStr);
        }

        return wrapper;
    }

    public static GetAxisAngleWrapper handleGetMotorSpeed(byte[] data) {
        GetAxisAngleWrapper wrapper = new GetAxisAngleWrapper();

        String dataStr = BluetoothHelper.asciiToString(data);
        String[] angleStr;

        angleStr = dataStr.split(",");

        if(angleStr[0] != null) {
            wrapper.setAxis1Angle(angleStr[0]);
        }
        if(angleStr[1] != null) {
            wrapper.setAxis2Angle(angleStr[1]);
        }

        return wrapper;
    }

}
