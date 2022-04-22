package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;

/**
 * 方法不存在异常处理器
 *
 * @author pengsenhao
 */
public class HttpRequestMethodNotSupportedExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 104;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == HttpRequestMethodNotSupportedException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(request.getRequestURI() + " 方法不存在", exception, request);
    }

}
