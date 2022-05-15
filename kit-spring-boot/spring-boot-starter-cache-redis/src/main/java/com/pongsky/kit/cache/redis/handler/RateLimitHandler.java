package com.pongsky.kit.cache.redis.handler;

import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 限流 key 处理器
 *
 * @author pengsenhao
 */
public interface RateLimitHandler {

    /**
     * 是否放行
     *
     * @param request   request
     * @param signature signature
     * @param method    method
     * @return 是否放行
     */
    boolean release(HttpServletRequest request, MethodSignature signature, Method method);

    /**
     * 获取限流 key
     *
     * @param request   request
     * @param signature signature
     * @param method    method
     * @return 获取限流 key
     */
    String getKey(HttpServletRequest request, MethodSignature signature, Method method);

    /**
     * 获取令牌每秒恢复个数
     *
     * @param request   request
     * @param signature signature
     * @param method    method
     * @return 获取令牌每秒恢复个数
     */
    int getBucketRate(HttpServletRequest request, MethodSignature signature, Method method);

    /**
     * 获取令牌桶大小
     *
     * @param request   request
     * @param signature signature
     * @param method    method
     * @return 获取令牌桶大小
     */
    int getBucketMax(HttpServletRequest request, MethodSignature signature, Method method);

}
