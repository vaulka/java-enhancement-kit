package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.web.utils.SpringUtils;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * 方法不存在异常处理器
 *
 * @author pengsenhao
 */
public class HttpRequestMethodNotSupportedExceptionFailProcessor implements BaseFailProcessor<HttpRequestMethodNotSupportedException> {

    @Override
    public Integer code() {
        return 104;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == HttpRequestMethodNotSupportedException.class;
    }

    @Override
    public Object exec(HttpRequestMethodNotSupportedException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return exception.getLocalizedMessage();
        }
        String message = MessageFormat.format("{0} 方法不存在", request.getRequestURI());
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
