package com.tkhoon.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    // 格式化日期与时间
    public static String formatDatetime(long timestamp) {
        return datetimeFormat.format(new Date(timestamp));
    }

    // 格式化日期
    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    // 格式化时间
    public static String formatTime(long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    // 获取当前日期与时间
    public static String getCurrentDatetime() {
        return datetimeFormat.format(new Date());
    }

    // 获取当前日期
    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }

    // 获取当前时间
    public static String getCurrentTime() {
        return timeFormat.format(new Date());
    }
}
