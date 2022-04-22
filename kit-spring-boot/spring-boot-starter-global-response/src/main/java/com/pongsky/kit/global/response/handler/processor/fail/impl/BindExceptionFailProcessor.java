package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.validation.utils.ValidationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

/**
 * param 数据校验异常处理器
 *
 * @author pengsenhao
 */
public class BindExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 108;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == BindException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String errorMessage = ValidationUtils.getErrorMessage(((BindException) exception).getBindingResult());
        return this.buildResult(errorMessage, exception, request);
    }

}
