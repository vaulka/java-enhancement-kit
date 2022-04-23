package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.HttpException;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP 请求异常处理器
 *
 * @author pengsenhao
 */
public class HttpExceptionFailProcessor implements BaseFailProcessor<HttpException> {

    @Override
    public Integer code() {
        return 204;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == HttpException.class;
    }

    @Override
    public Object exec(HttpException exception, HttpServletRequest request, ApplicationContext applicationContext) {
        String message = exception.getLocalizedMessage();
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
