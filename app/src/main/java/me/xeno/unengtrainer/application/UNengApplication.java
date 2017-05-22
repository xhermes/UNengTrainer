package me.xeno.unengtrainer.application;

import android.app.Application;

import com.clj.fastble.BleManager;

import me.xeno.unengtrainer.util.CrashHandler;

/**
 * Created by Administrator on 2017/5/21.
 */

public class UNengApplication extends Application {

    private BleManager mBleManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //do init work
        CrashHandler.getInstance().init(this);
        DataManager.getInstance().init(this);


    }

}
