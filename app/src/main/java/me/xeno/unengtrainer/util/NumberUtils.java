package me.xeno.unengtrainer.util;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/7/10.
 */

public class NumberUtils {
    public static String showNumber1Decimal(double number) {
        DecimalFormat df = new DecimalFormat("0.0");
        return String.valueOf(df.format(number));
    }

    public static String showNumber1Decimal(String number) {
        DecimalFormat df = new DecimalFormat("0.0");
        return String.valueOf(df.format(Double.valueOf(number)));
    }

    public static String showNumber1Decimal(float number) {
        DecimalFormat df = new DecimalFormat("0.0");
        return String.valueOf(df.format(number));
    }
}
