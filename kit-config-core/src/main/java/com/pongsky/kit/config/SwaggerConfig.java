package com.pongsky.kit.config;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.text.MessageFormat;
import java.util.List;

/**
 * Swagger 配置
 *
 * @author pengsenhao
 **/
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    private final SystemConfig systemConfig;

    /**
     * 创建 swagger 配置信息
     *
     * @return 创建 swagger 配置信息
     * @author pengsenhao
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title(MessageFormat.format("{0}-{1} API Docs", systemConfig.getActive(), systemConfig.getServiceName()))
                        .version(systemConfig.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(HttpAuthenticationScheme.JWT_BEARER_BUILDER.name(HttpHeaders.AUTHORIZATION).build()))
                .securityContexts(List.of(SecurityContext.builder()
                        .securityReferences(List.of(
                                SecurityReference.builder()
                                        .reference(HttpHeaders.AUTHORIZATION)
                                        .scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")})
                                        .build()))
                        .build()));
    }

}