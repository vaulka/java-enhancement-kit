package com.pongsky.kit.global.response.handler.processor.supports.springfox;

import com.pongsky.kit.common.global.response.processor.supports.BaseSupportsReturnTypeProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import springfox.documentation.swagger.web.ApiResourceController;

/**
 * 不拦截 {@link ApiResourceController} 相关资源请求
 *
 * @author pengsenhao
 */
@ConditionalOnBean(ApiResourceController.class)
public class ApiResourceControllerSupportsReturnTypeProcessor implements BaseSupportsReturnTypeProcessor {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getContainingClass() == ApiResourceController.class;
    }

}
