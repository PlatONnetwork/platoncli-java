package com.cicdi.jcli.util;

import java.io.Console;
import java.util.Locale;
import java.util.Scanner;

/**
 * 字符串工具类
 *
 * @author haypo
 * @date 2021/1/6
 */
public class StringUtil {
    private final static Console CONSOLE = System.console();

    public static void printEfl(String format, Object... args) {
        System.err.printf(format, args);
        System.err.println();
    }

    public static void info(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    public static void warn(String format, Object... args) {
        System.err.printf(format, args);
        System.err.println();
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

    /**
     * @return 从键盘读取密码
     */
    public static String readPassword() {
        String fmt = ResourceBundleUtil.getTextString("readPassword") + ":";
        return readPassword(fmt);
    }

    public static String readPassword(String fmt) {
        char[] passwd;
        if (CONSOLE == null) {
            System.out.println(fmt);
            passwd = new Scanner(System.in).nextLine().toCharArray();
        } else {
            passwd = CONSOLE.readPassword(fmt);
        }
        if (passwd.length < 6) {
            System.out.println(ResourceBundleUtil.getTextString("passwordLengthError"));
            return readPassword();
        } else {
            return new String(passwd);
        }
    }

    /**
     * @return 从键盘读取新密码
     */
    public static String readNewPasswordTwice() {
        String password = readPassword(ResourceBundleUtil.getTextString("readNewPassword") + ": ");
        String passwordAgain = readPassword(ResourceBundleUtil.getTextString("readNewPasswordAgain") + ": ");
        if (!password.equals(passwordAgain)) {
            System.out.println("passwords mismatch");
            return readNewPasswordTwice();
        }
        return password;
    }

    /**
     * @return 从键盘读取两次密码
     */
    public static String readPasswordTwice() {
        System.out.println(ResourceBundleUtil.getTextString("readPassword") + ": ");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        System.out.println(ResourceBundleUtil.getTextString("readPasswordAgain") + ": ");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            System.out.println("passwords mismatch");
            return readPasswordTwice();
        }
        return password;
    }

    /**
     * @return 从键盘读取y或n
     */
    public static boolean readYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        return Common.LETTER_Y.equals(scanner.nextLine().toLowerCase(Locale.ROOT));
    }

    /**
     * @return 从键盘读取私钥
     */
    public static String readPrivateKey() {
        System.out.println(ResourceBundleUtil.getTextString("readPrivateKey") + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * @return 从键盘读取助记词
     */
    public static String readMnemonic() {
        System.out.println(ResourceBundleUtil.getTextString("readMnemonic") + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}
