package com.cicdi.jcli.util;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author haypo
 * @date 2021/1/6
 */
public class StringUtil {
    public static final String CHN_REGEX = "[\u4e00-\u9fa5]";

    /**
     * @param str 待检测字符串
     * @return 是否包含中文字符
     */
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
        System.out.println("需要用户输入密码:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readNewPasswordTwice() {
        System.out.println("需要用户输入新密码:");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        System.out.println("需要用户再次输入新密码:");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            throw new RuntimeException("两次输入的密码不一致");
        }
        return password;
    }

    public static String readPasswordTwice() {
        System.out.println("需要用户输入密码:");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        System.out.println("需要用户再次输入密码:");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            throw new RuntimeException("两次输入的密码不一致");
        }
        return password;
    }

    public static boolean readYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        return "y".equals(scanner.nextLine().toLowerCase(Locale.ROOT));
    }

    public static String readPrivateKey() {
        System.out.println("需要用户输入私钥（十六进制字符串）：");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readMnemonic() {
        System.out.println("需要用户输入助记词:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readMnemonicAgain() {
        System.out.println("需要用户再次输入助记词:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
