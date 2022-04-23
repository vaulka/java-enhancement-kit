package com.pongsky.kit.global.response.handler.processor.supports.impl.springfox;

import com.pongsky.kit.global.response.handler.processor.supports.BaseSupportsReturnTypeProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import springfox.documentation.oas.web.OpenApiControllerWebMvc;

/**
 * 不拦截 {@link OpenApiControllerWebMvc} 相关资源请求
 *
 * @author pengsenhao
 */
@ConditionalOnBean(OpenApiControllerWebMvc.class)
public class OpenApiControllerWebMvcSupportsReturnTypeProcessor implements BaseSupportsReturnTypeProcessor {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getContainingClass() == OpenApiControllerWebMvc.class;
    }

}
