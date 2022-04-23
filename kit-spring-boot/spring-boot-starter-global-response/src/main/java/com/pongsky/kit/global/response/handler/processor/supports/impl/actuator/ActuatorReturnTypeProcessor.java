package com.pongsky.kit.global.response.handler.processor.supports.impl.actuator;

import com.pongsky.kit.global.response.handler.processor.supports.BaseSupportsReturnTypeProcessor;
import com.pongsky.kit.web.utils.SpringUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;

/**
 * 不拦截 Actuator 相关资源请求
 *
 * @author pengsenhao
 */
@ConditionalOnClass(Endpoint.class)
public class ActuatorReturnTypeProcessor implements BaseSupportsReturnTypeProcessor {

    /**
     * Actuator 默认 URL 前缀
     */
    private static final String DEFAULT_ACTUATOR_URL_PREFIX = "/actuator";

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        return request != null && request.getRequestURI().startsWith(DEFAULT_ACTUATOR_URL_PREFIX);
    }

}
