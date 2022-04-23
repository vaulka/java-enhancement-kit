package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
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
public class MethodArgumentNotValidExceptionFailProcessor implements BaseFailProcessor<MethodArgumentNotValidException> {

    @Override
    public Integer code() {
        return 107;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MethodArgumentNotValidException.class;
    }

    @Override
    public Object exec(MethodArgumentNotValidException exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String message = ValidationUtils.getErrorMessage(exception.getBindingResult());
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
