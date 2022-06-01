package com.pongsky.kit.global.response.handler.processor.supports.springfox;

import com.pongsky.kit.common.global.response.processor.supports.BaseSupportsReturnTypeProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import springfox.documentation.swagger2.web.Swagger2ControllerWebMvc;

/**
 * 不拦截 {@link Swagger2ControllerWebMvc} 相关资源请求
 *
 * @author pengsenhao
 */
@ConditionalOnBean(Swagger2ControllerWebMvc.class)
public class Swagger2ControllerWebMvcSupportsReturnTypeProcessor implements BaseSupportsReturnTypeProcessor {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getContainingClass() == Swagger2ControllerWebMvc.class;
    }

}
