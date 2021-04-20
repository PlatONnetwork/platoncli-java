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
        System.out.println(ResourceBundleUtil.getTextString("readPassword") + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readNewPasswordTwice() {
        System.out.println(ResourceBundleUtil.getTextString("readNewPassword") + ": ");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        System.out.println(ResourceBundleUtil.getTextString("readNewPasswordAgain") + ": ");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            throw PASSWORDS_MISMATCH_EXCEPTION;
        }
        return password;
    }

    public static String readPasswordTwice() {
        System.out.println(ResourceBundleUtil.getTextString("readPassword") + ": ");
        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        System.out.println(ResourceBundleUtil.getTextString("readPasswordAgain") + ": ");
        String passwordAgain = scanner.nextLine();
        if (!password.equals(passwordAgain)) {
            throw PASSWORDS_MISMATCH_EXCEPTION;
        }
        return password;
    }

    private static final RuntimeException PASSWORDS_MISMATCH_EXCEPTION = new RuntimeException("passwords mismatch");

    public static boolean readYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        return "y".equals(scanner.nextLine().toLowerCase(Locale.ROOT));
    }

    public static String readPrivateKey() {
        System.out.println(ResourceBundleUtil.getTextString("readPrivateKey") + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String readMnemonic() {
        System.out.println(ResourceBundleUtil.getTextString("readMnemonic") + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}
