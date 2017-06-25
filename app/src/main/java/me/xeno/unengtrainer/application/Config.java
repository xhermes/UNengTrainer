package me.xeno.unengtrainer.application;

/**
 * Created by Administrator on 2017/5/21.
 */

public class Config {

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

    public static final int RUN_AXIS_STOP = 0;//的运行/停止
    public static final int RUN_AXIS_POSITIVE = 1;//正方向连续运行(直到正限位或者报警)
    public static final int RUN_AXIS_NEGATIVE = 2;//负方向连续运行(直到负限位或者报警)

    public static final String SP_AXIS_ANGLE_1 = "SP_AXIS_ANGLE_1";
    public static final String SP_AXIS_ANGLE_2 = "SP_AXIS_ANGLE_2";
    public static final String SP_MOTOR_SPEED_1 = "SP_MOTOR_SPEED_1";
    public static final String SP_MOTOR_SPEED_2 = "SP_MOTOR_SPEED_2";
}
