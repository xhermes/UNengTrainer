package me.xeno.unengtrainer.v2.transport.frame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.xeno.unengtrainer.util.CommonUtils;
import me.xeno.unengtrainer.util.Logger;

public class FrameWriter {

    private static final byte FRAME_HEADER = (byte) 0xFB;
    private static final byte FRAME_END = (byte) 0x0D;


    /**
     * @param type 命令号
     * @param data 写入数据，没有传null
     * @return 构建好的数据帧
     */
    public static byte[] createFrame(byte type, byte[] data) {
        ArrayList<Byte> dataFrame = new ArrayList<>();


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        //header
        outputStream.write(FRAME_HEADER);

        //type命令号
        outputStream.write(type);

        //长度，data为null时为0
        int length = 0;
        if (data != null) {
            length = data.length;
        }
        outputStream.write(length);

        //加入数据
        if (data != null) {
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //校验位 帧头+Type+Length+Data
        int crc = FRAME_HEADER + type + length;
        if(data != null) {
            for (byte b : data) {
                crc += b;
            }
        }
        Logger.debug("发送：crc = " + crc);
        //TODO 超过255忽略溢出部分，还需要考虑-127,255问题，不知道会不会有问题
        if (crc > 255) {
            crc = crc & 0xFF;
        }
        outputStream.write(crc);

        //帧尾
        outputStream.write(FRAME_END);

        byte[] result = outputStream.toByteArray();
//        byte[] result = new byte[]{FRAME_HEADER, type, length, FRAME_END};
        Logger.debug(CommonUtils.bytes2HexString(result));

        return result;
    }
}
