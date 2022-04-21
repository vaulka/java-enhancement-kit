package com.pongsky.kit.web.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * httpServletRequest 工具类
 *
 * @author pengsenhao
 */
public class HttpServletRequestUtils {

    /**
     * 获取 request body 数据
     *
     * @param request request
     * @return 获取 request body 数据
     * @throws IOException IOException
     */
    public static String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = request.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            stringBuilder.append(str);
        }
        return StringUtils.isNotBlank(stringBuilder.toString())
                ? stringBuilder.toString()
                : null;
    }

}
