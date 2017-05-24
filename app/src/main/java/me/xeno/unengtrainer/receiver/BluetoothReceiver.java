package me.xeno.unengtrainer.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.xeno.unengtrainer.util.Logger;

/**
 * Created by xeno on 2017/5/24.
 */

public class BluetoothReceiver extends BroadcastReceiver {

    private OnBluetoothStateChangeListener mListener;

    public BluetoothReceiver(OnBluetoothStateChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR);
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                Logger.info("手机蓝牙关闭");
                mListener.onBluetoothTurnOff();
                break;

            case BluetoothAdapter.STATE_ON:
                Logger.info("手机蓝牙开启");
                mListener.onBluetoothTurnOn();
                break;
        }
    }

    public interface OnBluetoothStateChangeListener {
        void onBluetoothTurnOff();
        void onBluetoothTurnOn();
    }
}
