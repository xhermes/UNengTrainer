package me.xeno.unengtrainer.util;

/**
 * Created by Administrator on 2017/5/21.
 */

public class CommonUtils {
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

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
