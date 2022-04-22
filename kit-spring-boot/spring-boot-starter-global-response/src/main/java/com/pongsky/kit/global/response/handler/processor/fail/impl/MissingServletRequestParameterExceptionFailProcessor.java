package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;

/**
 * param 数据校验异常处理器
 *
 * @author pengsenhao
 */
public class MissingServletRequestParameterExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 108;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MissingServletRequestParameterException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
