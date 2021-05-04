package me.xeno.unengtrainer.v2.transport;


public class BluetoothHelper {
    /**
     * handle received bytes needs to be translated into strings.
     *
     * @param bytes
     * @return
     */
    public static String asciiToString(byte[] bytes) {
        //由上游接收到length=0的包问题而来，需要在上游忽略length=0的包，不执行动作
        //FIXME java.lang.NullPointerException: Attempt to get length of null array
        char[] buf = new char[bytes.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) bytes[i];
            sb.append(buf[i]);
        }
        return sb.toString();
    }

    /**
     * @param b 字节数组
     * @return 16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

}
