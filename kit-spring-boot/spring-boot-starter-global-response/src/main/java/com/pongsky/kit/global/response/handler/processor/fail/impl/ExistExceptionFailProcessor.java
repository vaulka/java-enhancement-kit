package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.DoesNotExistException;
import com.pongsky.kit.common.exception.ExistException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 存在异常处理器
 *
 * @author pengsenhao
 */
public class ExistExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 201;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == ExistException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
