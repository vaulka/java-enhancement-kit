package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.web.core.utils.SpringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.servlet.http.HttpServletRequest;

/**
 * request body 数据转换异常处理器
 *
 * @author pengsenhao
 */
public class HttpMessageNotReadableExceptionFailProcessor implements BaseFailProcessor<HttpMessageNotReadableException> {

    @Override
    public Integer code() {
        return 106;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == HttpMessageNotReadableException.class;
    }

    /**
     * 默认错误信息
     */
    private static final String MESSAGE = "request body 数据转换异常";

    @Override
    public Object exec(HttpMessageNotReadableException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return MESSAGE;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(MESSAGE, exception, request) : MESSAGE;
    }

}
