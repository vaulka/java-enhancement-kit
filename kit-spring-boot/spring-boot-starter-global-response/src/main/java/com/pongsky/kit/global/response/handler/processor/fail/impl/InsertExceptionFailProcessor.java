package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.InsertException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 保存异常处理器
 *
 * @author pengsenhao
 */
public class InsertExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 503;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == InsertException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
