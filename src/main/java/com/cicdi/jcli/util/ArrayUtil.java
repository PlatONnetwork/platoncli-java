package com.cicdi.jcli.util;

/**
 * @author haypo
 * @date 2020/12/28
 */
public class ArrayUtil {
    public static String[] skip(int n, String[] src) {
        String[] dest = new String[src.length - 1];
        System.arraycopy(src, n, dest, 0, dest.length);
        return dest;
    }

    public static String[] skip1(String[] src) {
        return skip(1, src);
    }
}
