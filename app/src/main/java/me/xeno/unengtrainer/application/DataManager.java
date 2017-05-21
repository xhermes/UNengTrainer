package me.xeno.unengtrainer.application;

import android.content.Context;

import com.clj.fastble.BleManager;

/**
 * Created by Administrator on 2017/5/21.
 */

public class DataManager {

    private static DataManager instance;

    private BleManager mBleManager;

    public static synchronized DataManager getInstance() {
        if(instance ==null)
            instance = new DataManager();
        return instance;
    }

    public void init(Context context) {
        //init
        mBleManager = new BleManager(context);
    }

    public BleManager getBleManager() {
        return mBleManager;
    }
}
