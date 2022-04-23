package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.ValidationException;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 校验异常处理器
 *
 * @author pengsenhao
 */
public class ValidationExceptionFailProcessor implements BaseFailProcessor<ValidationException> {

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
    public Object exec(ValidationException exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String message = exception.getLocalizedMessage();
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
