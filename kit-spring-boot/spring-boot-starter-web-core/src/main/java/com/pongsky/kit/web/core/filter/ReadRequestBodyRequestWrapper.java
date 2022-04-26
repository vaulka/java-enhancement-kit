package com.pongsky.kit.web.core.filter;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 读取 Request Body 数据
 *
 * @author pengsenhao
 **/
public interface ReadRequestBodyRequestWrapper {

    /**
     * 读取 Request Body 数据
     *
     * @param request request
     * @return Request Body 字符串数据
     */
    default String readRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
        return stringBuilder.toString();
    }

}
