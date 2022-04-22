package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.DoesNotExistException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 不存在异常处理器
 *
 * @author pengsenhao
 */
public class DoesNotExistExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 202;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == DoesNotExistException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(exception.getLocalizedMessage(), exception, request);
    }

}
