package com.pongsky.kit.cache.redis.handler;

import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 防重处理器
 *
 * @author pengsenhao
 */
public interface PreventDuplicationHandler {

    /**
     * 是否放行
     *
     * @param request   request
     * @param signature signature
     * @param method    method
     * @return 是否放行
     */
    boolean release(HttpServletRequest request, MethodSignature signature, Method method);

}
