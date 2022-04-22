package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.validation.utils.ValidationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;

/**
 * request body 数据校验异常处理器
 *
 * @author pengsenhao
 */
public class MethodArgumentNotValidExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 107;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MethodArgumentNotValidException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String errorMessage = ValidationUtils.getErrorMessage(((MethodArgumentNotValidException) exception).getBindingResult());
        return this.buildResult(errorMessage, exception, request);
    }

}
