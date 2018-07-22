package me.xeno.unengtrainer.application;

/**
 * Created by Administrator on 2017/5/21.
 */

public class Config {

    public static final int DEBUG_MODE_OFF = 0;
    public static final int DEBUG_MODE_CTRL = 1;//调试主控界面时使用
    public static final int DEBUG_MODE_BLUETOOTH = 2;//调试蓝牙界面时使用

    //TODO 调试模式，发版时记得调成off
    public static final int DEBUG_MODE = DEBUG_MODE_OFF;
    public static boolean isDebugging() {
        return DEBUG_MODE > 0;
    }

    public static final int GET_STATUS_PERIOD = 2;//获取状态间隔，单位秒，2018.7.15状态加入校准零位是否完成信息
    public static final int GET_BATTERY_PERIOD = 120;//获取电压间隔，单位秒
    public static final int GET_ANGLE_PERIOD = 1500;//获取角度间隔，单位豪秒
    public static final int GET_ANGLE_PERIOD_INFREQUENTLY = 4000;//获取角度间隔，单位豪秒


    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public static final byte DATA_TYPE_GET_STATUS = 0x02;//查询步进电机状态 & 回归零点命令返回值 & 下位机主动发送异常状态
    public static final byte DATA_TYPE_ENABLE = 0x03;//使能步进电机
    public static final byte DATA_TYPE_SWITCH_BRAKE = 0x04;//打开/关闭电机刹车
    public static final byte DATA_TYPE_MAKE_ZERO = 0x05;//回归零点
    public static final byte DATA_TYPE_SET_AXIS_ANGLE = 0x06;//设置1,2轴角度
    public static final byte DATA_TYPE_RUN_AXIS = 0x07;//1,2轴单轴运行/停止
    public static final byte DATA_TYPE_SET_AXIS_SPEED = 0x08;//设置轴运行速度
    public static final byte DATA_TYPE_GET_AXIS_ANGLE = 0x09;//获取1,2轴当前角度
    public static final byte DATA_TYPE_SET_MOTOR_SPEED = 0x0A;//设置第 1,2 发球电机的速度
    public static final byte DATA_TYPE_GET_BATTERY_VOLTAGE = 0x0B;//获取电池电压
    public static final byte DATA_TYPE_GET_MOTOR_SPEED = 0x0C;//获取第 1,2 发球电机速度

    public static final int RUN_AXIS_STOP = 0;//的运行/停止
    public static final int RUN_AXIS_POSITIVE = 1;//正方向连续运行(直到正限位或者报警)
    public static final int RUN_AXIS_NEGATIVE = 2;//负方向连续运行(直到负限位或者报警)
    public static final int RUN_AXIS_PERIOD = 100;//单轴运行发送间隔，单位ms

    public static final String SP_AXIS_ANGLE_1 = "SP_AXIS_ANGLE_1";
    public static final String SP_AXIS_ANGLE_2 = "SP_AXIS_ANGLE_2";
    public static final String SP_MOTOR_SPEED_1 = "SP_MOTOR_SPEED_1";
    public static final String SP_MOTOR_SPEED_2 = "SP_MOTOR_SPEED_2";

    public static final int AXIS_STATUS_RUNNING_STOP = 0;
    public static final int AXIS_STATUS_RUNNING_RUNNING = 1;
}
