package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.CircuitBreakerException;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.web.core.utils.SpringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 断路异常处理器
 *
 * @author pengsenhao
 */
public class CircuitBreakerExceptionFailProcessor implements BaseFailProcessor<CircuitBreakerException> {

    @Override
    public Integer code() {
        return 502;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == CircuitBreakerException.class;
    }

    /**
     * 默认错误信息
     */
    private static final String MESSAGE = "服务器出了点小差，请稍后再试";

    @Override
    public Object exec(CircuitBreakerException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return MESSAGE;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(MESSAGE, exception, request) : MESSAGE;
    }

}
