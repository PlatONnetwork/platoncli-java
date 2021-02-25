package com.cicdi.jcli.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具
 *
 * @author haypo
 * @date 2020/12/25
 */
public class TimeUtil {
    public static String getNanoTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss-SSS"));
    }

    /**
     * @return 获得时间戳
     */
    public static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
