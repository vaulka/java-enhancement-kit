package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.web.utils.SpringUtils;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.validation.utils.ValidationUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;

/**
 * request body 数据校验异常 / param 数据校验异常 处理器
 *
 * @author pengsenhao
 */
public class BindExceptionFailProcessor implements BaseFailProcessor<BindException> {

    @Override
    public Integer code() {
        return 107;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == MethodArgumentNotValidException.class
                || exception.getClass() == BindException.class;
    }

    @Override
    public Object exec(BindException exception) {
        String message = ValidationUtils.getErrorMessage(exception.getBindingResult());
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return message;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
