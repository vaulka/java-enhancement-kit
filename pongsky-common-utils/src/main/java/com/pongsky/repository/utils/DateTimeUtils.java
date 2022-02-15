package com.pongsky.repository.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author pengsenhao
 * @description
 * @create 2021-08-03
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
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-01-13 3:31 下午
     */
    public static String localDateTime2String(LocalDateTime localDateTime) {
        return LOCAL_DATE_TIME_FORMAT.format(localDateTime);
    }

}
