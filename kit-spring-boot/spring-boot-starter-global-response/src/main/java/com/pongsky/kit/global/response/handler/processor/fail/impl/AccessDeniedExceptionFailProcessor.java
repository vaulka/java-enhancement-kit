package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问权限异常处理器
 *
 * @author pengsenhao
 */
@ConditionalOnClass(AccessDeniedException.class)
public class AccessDeniedExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 403;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == AccessDeniedException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult("访问凭证已过期，请重新登录", exception, request);
    }

}
