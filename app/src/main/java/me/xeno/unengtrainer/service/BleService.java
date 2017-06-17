package me.xeno.unengtrainer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/6/11.
 * //TODO 需要用此service来保存BLE连接成功后的状态，可以参考github fastble源码
 */

public class BleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
