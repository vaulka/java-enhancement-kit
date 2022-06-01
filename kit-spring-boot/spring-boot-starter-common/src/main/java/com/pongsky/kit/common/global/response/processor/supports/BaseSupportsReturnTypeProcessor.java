package com.pongsky.kit.common.global.response.processor.supports;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;

/**
 * 是否执行全局响应处理器
 *
 * @author pengsenhao
 */
public interface BaseSupportsReturnTypeProcessor {

    /**
     * 是否执行全局响应处理器
     * <p>
     * 如果返回值为 true，则直接放行
     *
     * @param returnType returnType
     * @return 是否执行全局响应处理器
     * @see HandlerMethodReturnValueHandler#supportsReturnType(MethodParameter)
     */
    boolean supportsReturnType(MethodParameter returnType);

}
