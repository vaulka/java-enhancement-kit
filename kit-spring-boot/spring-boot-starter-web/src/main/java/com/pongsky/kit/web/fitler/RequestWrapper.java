package com.pongsky.kit.web.fitler;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 替换 HttpServletRequest，实现 request body 数据多次读取
 *
 * @author pengsenhao
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 存储 request body 数据的容器
     */
    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        body = readBody(request);
    }

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    public byte[] readBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        if (request.getContentType().contains(CONTENT_TYPE)) {
            request.getParameterMap().forEach((k, v) -> {
                if (v == null) {
                    return;
                }
                stringBuilder.append(k).append("=").append(v.length == 1 ? v[0] : Arrays.toString(v)).append("&");
            });
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(),
                    StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage(), e);
            }
        }
        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

}