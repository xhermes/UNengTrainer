package me.xeno.unengtrainer.presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import me.xeno.unengtrainer.application.DataManager;
import me.xeno.unengtrainer.view.activity.MainActivity;

/**
 * Created by xeno on 2017/6/21.
 */

public class MainPresenter {

    private MainActivity mActivity;

    public MainPresenter(MainActivity activity) {
        this.mActivity = activity;
    }

}
