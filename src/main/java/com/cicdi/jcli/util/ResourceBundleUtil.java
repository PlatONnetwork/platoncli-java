package com.cicdi.jcli.util;

import java.util.ResourceBundle;

/**
 * @author haypo
 * @date 2021/4/8
 */
public class ResourceBundleUtil {
    public static String getTextString(String key) {
        return ResourceBundle.getBundle("text").getString(key);
    }
}
