package com.cicdi.jcli.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haypo
 * @date 2021/1/6
 */
public class StringUtil {
    public static final String CHN_REGEX = "[\u4e00-\u9fa5]";

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile(CHN_REGEX);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断字符串是否为空
     *
     * @param o 字符串
     * @return 字符串是否为空
     */
    public static boolean isBlank(String o) {
        return o == null || o.isEmpty();
    }

    public static String readPassword() {
        System.out.println("在线交易需要用户输入密码:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
