package com.pongsky.kit.web.core.filter;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 实现 Request Body 数据多次读取
 *
 * @author pengsenhao
 */
public class RepeatedlyReadRequestBodyRequestWrapper extends HttpServletRequestWrapper implements ReadRequestBodyRequestWrapper {

    /**
     * Request Body 数据
     */
    private final String requestBody;

    public RepeatedlyReadRequestBodyRequestWrapper(HttpServletRequest request) {
        super(request);
        requestBody = this.readRequestBody(request);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
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