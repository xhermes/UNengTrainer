package me.xeno.unengtrainer.model;

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
            float percentage = 0;
            if(voltageFloat >= 30 && voltageFloat <= 41.5f) {
                percentage =  (voltageFloat - LOWER_LIMIT) / (HIGHER_LIMIT - LOWER_LIMIT) ;
            } else if(voltageFloat < LOWER_LIMIT) {
                percentage = 1;
            } else if(voltageFloat > HIGHER_LIMIT) {
                percentage = 100;
            }
            return percentage + "%";
        }
        return "-%";
    }

}
