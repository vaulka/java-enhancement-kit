package com.pongsky.springcloud.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author pengsenhao
 */
public class DateTimeUtils {

    /**
     * LocalDateTime 格式化输出
     */
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 Date 转成 LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 将 String yyyy-MM-dd HH:mm:ss 转成 LocalDateTime
     *
     * @param date String yyyy-MM-dd HH:mm:ss
     * @return LocalDateTime
     */
    public static LocalDateTime string2LocalDateTime(String date) {
        return LocalDateTime.parse(date, LOCAL_DATE_TIME_FORMAT);
    }

    /**
     * 将 LocalDateTime 转成 String yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime LocalDateTime
     * @return String yyyy-MM-dd HH:mm:ss
     * @author pengsenhao
     */
    public static String localDateTime2String(LocalDateTime localDateTime) {
        return LOCAL_DATE_TIME_FORMAT.format(localDateTime);
    }

}
