package com.pongsky.kit.global.response.handler.processor.supports.springdoc;

import com.pongsky.kit.common.global.response.processor.supports.BaseSupportsReturnTypeProcessor;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;

/**
 * 不拦截 {@link OpenApiWebMvcResource} 相关资源请求
 *
 * @author pengsenhao
 */
@ConditionalOnClass(OpenApiWebMvcResource.class)
public class OpenApiWebMvcResourceSupportsReturnTypeProcessor implements BaseSupportsReturnTypeProcessor {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getContainingClass() == OpenApiWebMvcResource.class;
    }

}
