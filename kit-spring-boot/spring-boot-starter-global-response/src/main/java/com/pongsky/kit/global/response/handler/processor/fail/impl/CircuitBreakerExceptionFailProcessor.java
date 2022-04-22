package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.CircuitBreakerException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 断路异常处理器
 *
 * @author pengsenhao
 */
public class CircuitBreakerExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 502;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == CircuitBreakerException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult("服务器出了点小差，请稍后再试", exception, request);
    }

}
