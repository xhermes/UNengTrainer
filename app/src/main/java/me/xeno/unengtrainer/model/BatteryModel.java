package me.xeno.unengtrainer.model;

import me.xeno.unengtrainer.util.NumberUtils;

/**
 * Created by xeno on 2018/3/1.
 * 电量信息
 */

public class BatteryModel {

    private static float LOWER_LIMIT = 30;
    private static float HIGHER_LIMIT = 41.5f;

    public static String getCurrentBatteryPercentage(String voltage) {

        float voltageFloat = 0;
        try {
            voltageFloat = Float.valueOf(voltage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(voltageFloat > 0) {
            String percentageStr = null;
            if(voltageFloat >= 30 && voltageFloat <= 41.5f) {
                float percentage =  (voltageFloat - LOWER_LIMIT) * 100 / (HIGHER_LIMIT - LOWER_LIMIT) ;
                //保留一位小数
                percentageStr = NumberUtils.showNumber1Decimal(percentage);
            } else if(voltageFloat < LOWER_LIMIT) {
                percentageStr = "1";
            } else if(voltageFloat > HIGHER_LIMIT) {
                percentageStr = "100";
            }
            return percentageStr + "%";
        }
        return "-%";
    }

}
