package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口不存在异常处理器
 *
 * @author pengsenhao
 */
public class NoHandlerFoundExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 105;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == NoHandlerFoundException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(request.getRequestURI() + " 接口不存在", exception, request);
    }

}
