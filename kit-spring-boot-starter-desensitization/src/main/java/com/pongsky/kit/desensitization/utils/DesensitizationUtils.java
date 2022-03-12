package com.pongsky.kit.desensitization.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据脱敏工具类
 *
 * @author pengsenhao
 */
public class DesensitizationUtils {

    /**
     * 数据脱敏
     * 将字符串明文保留前 start 位 以及 后 end 位，中位用 * 代替
     * <p>
     * 如果字符串长度不足，则按明文处理
     *
     * @param str   字符串
     * @param start 明文保留前 start 位
     * @param end   明文保留后 end 位
     * @return 脱敏后的字符串
     * @author pengsenhao
     */
    public static String desensitization(String str, int start, int end) {
        if (StringUtils.isBlank(str) || 0 > start || 0 > end) {
            return str;
        } else if (start + end >= str.length()) {
            return str;
        }
        return StringUtils.left(str, start)
                .concat(StringUtils.leftPad("", str.length() - start - end, "*"))
                .concat(StringUtils.right(str, end));
    }

}
