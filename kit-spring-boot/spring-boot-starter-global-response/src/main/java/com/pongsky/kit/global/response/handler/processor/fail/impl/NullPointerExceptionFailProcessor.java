package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 空指针异常处理器
 *
 * @author pengsenhao
 */
public class NullPointerExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 506;
    }

    @Override
    public boolean isSprintStackTrace() {
        return true;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == NullPointerException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
