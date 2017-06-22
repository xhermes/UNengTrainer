package me.xeno.unengtrainer.listener;

/**
 * Created by xeno on 2017/5/17.
 */
public interface BleServiceListener {

    /**
     * receive data from trainer
     * @param data
     */
    void onReceiveData(String data);

}
