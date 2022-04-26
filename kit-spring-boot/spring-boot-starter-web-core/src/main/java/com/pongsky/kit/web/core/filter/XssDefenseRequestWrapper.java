package com.pongsky.kit.web.core.filter;


import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * XSS 防御
 *
 * @author pengsenhao
 */
public class XssDefenseRequestWrapper extends HttpServletRequestWrapper implements ReadRequestBodyRequestWrapper {

    /**
     * Request Body 数据
     */
    private final String requestBody;

    public XssDefenseRequestWrapper(HttpServletRequest request) {
        super(request);
        requestBody = this.readRequestBody(request);
    }

    /**
     * 允许完整范围的文本和结构体 HTML 列表
     */
    private static final Safelist SAFELIST = Safelist.relaxed();

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return StringUtils.isBlank(header)
                ? header
                : Jsoup.clean(header, SAFELIST);
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return StringUtils.isBlank(parameter)
                ? parameter
                : Jsoup.clean(parameter, SAFELIST);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null || parameterValues.length == 0) {
            return parameterValues;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String parameterValue = parameterValues[i];
            parameterValues[i] = Jsoup.clean(parameterValue, SAFELIST);
        }
        return parameterValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        if (parameterMap == null || parameterMap.size() == 0) {
            return parameterMap;
        }
        Map<String, String[]> paramMap = new HashMap<>(parameterMap.size() * 2);
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            key = StringUtils.isBlank(key)
                    ? key
                    : Jsoup.clean(key, SAFELIST);
            String[] values = entry.getValue();
            values = Arrays.stream(values)
                    .map(s -> StringUtils.isBlank(s)
                            ? s
                            : Jsoup.clean(s, SAFELIST))
                    .toArray(String[]::new);
            paramMap.put(key, values);
        }
        return paramMap;
    }

    @SneakyThrows
    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        String[] querySplit = queryString.split("&");
        if (querySplit.length == 0) {
            return queryString;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String query : querySplit) {
            String[] keyValue = query.split("=");
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.toString());
            key = Jsoup.clean(key, SAFELIST);
            stringBuilder.append("&").append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()));
            if (keyValue.length > 1) {
                keyValue[0] = "";
                String value = Arrays.stream(keyValue)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining());
                value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
                value = Jsoup.clean(value, SAFELIST);
                stringBuilder.append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
            }
        }
        stringBuilder.deleteCharAt(0);
        return stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() {
        String contentType = super.getContentType();
        ByteArrayInputStream inputStream;
        if (contentType == null
                || contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                || contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            String safeRequestBody = Jsoup.clean(requestBody, SAFELIST);
            inputStream = new ByteArrayInputStream(safeRequestBody.getBytes(StandardCharsets.UTF_8));
        } else {
            inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        }
        return new ServletInputStream() {
            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

}