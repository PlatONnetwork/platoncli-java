package com.cicdi.jcli.util;

import com.Ostermiller.util.CSVParser;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author haypo
 * @date 2021/4/8
 */
@Slf4j
public class ResourceBundleUtil {
    /**
     * 获取text资源包下的字符资源
     *
     * @param key 字符key
     * @return 字符value
     */
    public static String getTextString(String key) {
        return ResourceBundle.getBundle("text").getString(key);
    }

    /**
     * 获取template资源包下的字符资源
     *
     * @param key 字符key
     * @return 字符value
     */
    public static String getTemplateString(String key) {
        return ResourceBundle.getBundle("template").getString(key);
    }

    private static String getTemplateString(String key, Locale locale) {
        return ResourceBundle.getBundle("template", locale).getString(key);
    }

    public static void printTemplate(String key) {
        printTemplate(key, Locale.getDefault());
    }

    /**
     * 读取资源包下的json字符串并打印
     *
     * @param key 字符key
     */
    public static void printTemplate(String key, Locale locale) {
        try {
            String format;

            String data = getTemplateString(key, locale);
            InputStream inputStream = new ByteArrayInputStream(data.getBytes());
            CSVParser csvParser = new CSVParser(inputStream, ';');
            String[][] values = csvParser.getAllValues();


            format = genFormat(values, locale);
            for (String[] row : values) {
                System.out.printf(format, row[0], row[1], row[2], row[3]);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * s
     *
     * @param values
     * @param locale
     * @return
     */
    private static String genFormat(String[][] values, Locale locale) {
        String[] head = ResourceBundleUtil.getTemplateString("head", locale).split(";");
        int a1 = head[0].getBytes().length, a2 = head[1].getBytes().length, a3 = head[2].getBytes().length;
        for (String[] s : values) {
            if (s[0].length() > a1) {
                a1 = s[0].length();
            }
            if (s[1].length() > a2) {
                a2 = s[1].length();
            }
            if (s[2].length() > a3) {
                a3 = s[2].length();
            }
        }
        if (locale.getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            System.out.printf("%-" + (a1) + "s%-" + (a2) + "s%-" + (a3) + "s%s\n", head[0], head[1], head[2], head[3]);
        } else {
            System.out.printf("%-" + (a1 + 2) + "s%-" + (a2 + 3) + "s%-" + (a3 + 4) + "s%s\n", head[0], head[1], head[2], head[3]);
        }
        return "%-" + (a1 + 2) + "s%-" + (a2 + 3) + "s%-" + (a3 + 4) + "s%s\n";
    }

    private final static String chnFormat = "%-12s \t%-8s \t%-15s \t%s\n";
    private final static String engFormat = "%-12s \t%-8s \t%-9s \t%s\n";
}
