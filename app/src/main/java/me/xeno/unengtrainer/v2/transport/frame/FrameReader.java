package me.xeno.unengtrainer.v2.transport.frame;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.xeno.unengtrainer.application.Config;
import me.xeno.unengtrainer.v2.transport.instruction.InstReader;
import me.xeno.unengtrainer.listener.BleServiceListener;
import me.xeno.unengtrainer.util.Logger;

public class FrameReader {

    public static void handleReceivedData(byte[] dataPacket, final BleServiceListener listener) {
        //handle all data from bluetooth

        byte header = dataPacket[0];
        //FIXME java.lang.ArrayIndexOutOfBoundsException: length=1; index=1
        final byte type = dataPacket[1];
        byte length = dataPacket[2];
        byte[] data = null;
        if(length > 0){
            data = new byte[length];
            for(int i=3;i<3+length;i++){
                data[i-3] = dataPacket[i];
            }
        }
        int crc = dataPacket[dataPacket.length - 2];
        if(crc<0)
            crc+=256;
        byte end = dataPacket[dataPacket.length - 1];

        //回复帧头固定为0xFC，若非直接抛弃
        //TODO byte最大只有128，当然不可能是0xFC
//        if(header != 0xFC) {
//            Logger.warning("接收到蓝牙数据，帧头数据错误！");
//            return;
//        }

        // 使用校验位，校验数据，失败直接抛弃
        int crcCalculate;
        crcCalculate = header + type + length;
        if(data != null) {
            for (Byte b : data) {
                crcCalculate+=b;
            }
        }
        Logger.debug("接收：crcCalculate = " + crcCalculate);

        if(crcCalculate > 255) {
            crcCalculate = crcCalculate & 0xFF;
        }

        if(crc != crcCalculate) {
            Logger.warning("接收到蓝牙数据，校验错误！crc=" + crc + " ,crcCalculate=" + crcCalculate);
            return;
        }

        //打印数据长度
        Logger.debug("handleReceivedData: data Length = " + length);

        //根据命令号处理，需要切换到主线程
        final byte[] finalData = data;

        //如果data为null，直接放弃处理

        //TODO 5月份 手动版忽略收到的自动校准
        if(finalData != null) {
//        if(finalData != null || type == Config.DATA_TYPE_MAKE_ZERO) {
            Disposable disposable = Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            InstReader.read(type, finalData, listener);
                        }
                    });

        }
    }
}
