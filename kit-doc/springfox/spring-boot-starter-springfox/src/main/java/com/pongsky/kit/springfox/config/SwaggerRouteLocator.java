package com.pongsky.kit.springfox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.text.MessageFormat;

/**
 * Swagger 网关路由定位器
 *
 * @author pengsenhao
 */
@ConditionalOnClass(RouteLocator.class)
@ConditionalOnProperty(value = "doc.gateway-route-enabled", havingValue = "true")
public class SwaggerRouteLocator {

    /**
     * 服务端口号
     */
    @Value("${server.port}")
    private Integer port;

    /**
     * 注册 Swagger 网关路由定位器
     *
     * @param builder builder
     * @return Swagger 网关路由定位器
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("swagger", r -> r
                        .path("/v3/api-docs/**")
                        .filters(f -> f
                                .rewritePath("/v3/api-docs/(?<path>.*)", "/$\\{path}/v3/api-docs"))
                        .uri(MessageFormat.format("http://localhost:{0}", port.toString())))
                .build();
    }

}
