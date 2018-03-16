package me.xeno.unengtrainer.util;

import android.util.Log;

/**
 * Created by xeno on 2016/2/16.
 */
public class Logger {

    private static final String LOG_TAG = "xeno";
    public static final String LOG_TEST = "xtest";

    public static void info(String msg) {
        Log.i(LOG_TAG, msg);
    }
    public static void info(int msg) {
        Log.i(LOG_TAG, msg + "");
    }

    public static void error(String msg) {
        Log.e(LOG_TAG, msg);
    }
    public static void error(int msg) {
        Log.e(LOG_TAG, msg + "");
    }

    public static void warning(String msg) {
        Log.w(LOG_TAG, msg);
    }
    public static void warning(int msg) {
        Log.w(LOG_TAG, msg + "");
    }

    public static void debug(String msg) {
        Log.d(LOG_TAG, msg);
    }
    public static void debug(int msg) {
        Log.d(LOG_TAG, msg + "");
    }
}
