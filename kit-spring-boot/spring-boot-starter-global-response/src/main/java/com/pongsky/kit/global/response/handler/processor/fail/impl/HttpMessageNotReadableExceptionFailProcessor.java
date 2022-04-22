package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.servlet.http.HttpServletRequest;

/**
 * JSON 数据转换异常处理器
 *
 * @author pengsenhao
 */
public class HttpMessageNotReadableExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 106;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == HttpMessageNotReadableException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult("JSON 数据转换异常", exception, request);
    }

}
