package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.UpdateException;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 修改异常处理器
 *
 * @author pengsenhao
 */
public class UpdateExceptionFailProcessor implements BaseFailProcessor<UpdateException> {

    @Override
    public Integer code() {
        return 504;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == UpdateException.class;
    }

    @Override
    public Object exec(UpdateException exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String message = exception.getLocalizedMessage();
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
