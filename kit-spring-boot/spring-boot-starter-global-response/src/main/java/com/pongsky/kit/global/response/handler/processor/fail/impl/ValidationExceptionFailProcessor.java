package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.ValidationException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 校验异常处理器
 *
 * @author pengsenhao
 */
public class ValidationExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 101;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == ValidationException.class
                || exception.getClass() == ConstraintViolationException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
