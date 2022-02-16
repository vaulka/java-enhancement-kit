package com.pongsky.springcloud.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2021-12-04 23:34
 **/
@Configuration
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    /**
     * 创建 swagger 配置信息
     *
     * @return 创建 swagger 配置信息
     * @description 详细写出代码处理流程，方法内也要详细注释
     * @author pengsenhao
     * @date 2021-12-04 23:40
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title(MessageFormat.format("{0}-{1} API Docs", SystemConfig.getActive(), SystemConfig.getServiceName()))
                        .description("FBI, Open The Door")
                        .contact(new Contact("PONGSKY", "https://www.pongsky.com", "kelry@vip.qq.com"))
                        .version(SystemConfig.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(List.of(new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header")))
                .securityContexts(List.of(SecurityContext.builder()
                        .securityReferences(List.of(new SecurityReference(HttpHeaders.AUTHORIZATION,
                                new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")})))
                        .build()));
    }

}
