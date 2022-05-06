package com.pongsky.kit.web.core.filter;

import org.springframework.http.MediaType;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Request Body 数据多次读取
 *
 * @author pengsenhao
 */
public class RepeatedlyReadRequestBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String contentType = request.getContentType();
        ServletRequest servletRequest = request;
        if (contentType == null
                || contentType.contains(MediaType.APPLICATION_JSON_VALUE)
                || contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            servletRequest = new RepeatedlyReadRequestBodyRequestWrapper((HttpServletRequest) request);
        }
        chain.doFilter(servletRequest, response);
    }

}
