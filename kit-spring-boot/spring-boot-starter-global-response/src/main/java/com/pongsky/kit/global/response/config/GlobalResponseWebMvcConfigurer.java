package com.pongsky.kit.global.response.config;

import com.pongsky.kit.global.response.handler.GlobalResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 全局响应配置器
 *
 * @author pengsenhao
 */
@RequiredArgsConstructor
public class GlobalResponseWebMvcConfigurer implements WebMvcConfigurer {

    private final GlobalResponseHandler globalResponseHandler;

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(globalResponseHandler);
    }

}
