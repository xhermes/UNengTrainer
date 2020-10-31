package me.xeno.unengtrainer.util;

import android.util.Log;

import com.elvishew.xlog.XLog;

/**
 * Created by xeno on 2016/2/16.
 */
public class Logger {

    private static final String LOG_TAG = "xeno";
    public static final String LOG_TEST = "xtest";

    public static void info(String msg) {
        XLog.i(msg);
    }
    public static void info(int msg) {
        XLog.i(msg + "");
    }

    public static void error(String msg) {
        XLog.e(msg);
    }
    public static void error(int msg) {
        XLog.e(msg + "");
    }

    public static void warning(String msg) {
        XLog.w(msg);
    }
    public static void warning(int msg) {
        XLog.w(msg + "");
    }

    public static void debug(String msg) {
        XLog.d(msg);
    }
    public static void debug(int msg) {
        XLog.d(msg + "");
    }
}
