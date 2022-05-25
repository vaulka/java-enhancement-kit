package com.pongsky.kit.springfox.config;

import com.pongsky.kit.springfox.properties.SpringFoxProperties;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * SpringFox Swagger 配置
 * <p>
 * TODO <p>使用 openAPI 3.0 {@link io.swagger.v3.oas.annotations.tags.Tag} 注解会出现 API 文档错误。相关 <a href="https://github.com/springfox/springfox/issues/3668">issues</a> PR 至 3.0.1 version，等待官方发版</p>
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@ConditionalOnProperty(value = "doc.enabled", havingValue = "true", matchIfMissing = true)
public class SpringFoxConfig {

    private final SpringFoxProperties properties;
    private final ApplicationContext applicationContext;

    /**
     * 创建 Swagger 配置信息
     *
     * @return 创建 Swagger 配置信息
     */
    @Bean
    public Docket defaultDocket() {
        List<SecurityScheme> securitySchemes = properties.getRequestParameters().entrySet().stream()
                .map(e -> e.getValue().stream()
                        .map(v -> new ApiKey(v, v, e.getKey().name()))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<SecurityReference> collect = properties.getRequestParameters().values().stream()
                .flatMap(Collection::stream)
                .map(v -> SecurityReference.builder()
                        .reference(v)
                        .scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessAnything")})
                        .build())
                .collect(Collectors.toList());
        List<SecurityContext> securityContexts = Collections.singletonList(SecurityContext.builder()
                .securityReferences(collect)
                .build());
        if (properties.getGroups().size() == 0) {
            // 未填写组别则会有一个默认分组
            return new Docket(DocumentationType.OAS_30)
                    .apiInfo(new ApiInfoBuilder()
                            .title(properties.getTitle())
                            .description(properties.getDescription())
                            .version(properties.getVersion())
                            .build())
                    .select()
                    .apis(SpringFoxConfig.withMethodAnnotation())
                    .paths(PathSelectors.any())
                    .build()
                    .securitySchemes(securitySchemes)
                    .securityContexts(securityContexts);
        }
        List<String> displayNames = properties.getGroups().stream()
                .map(SpringFoxProperties.GroupOpenApi::getDisplayName)
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (displayNames.size() > 0) {
            throw new IllegalArgumentException(MessageFormat.format("组别名称 displayName {0} 出现重复",
                    displayNames));
        }
        List<String> groups = properties.getGroups().stream()
                .map(SpringFoxProperties.GroupOpenApi::getGroup)
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (groups.size() > 0) {
            throw new IllegalArgumentException(MessageFormat.format("组别路径 group {0} 出现重复",
                    groups));
        }
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
                ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        Docket defaultDocket = null;
        for (int i = 0; i < properties.getGroups().size(); i++) {
            SpringFoxProperties.GroupOpenApi groupOpenApi = properties.getGroups().get(i);
            Docket docket = new Docket(DocumentationType.OAS_30)
                    .apiInfo(new ApiInfoBuilder()
                            .title(properties.getTitle())
                            .description(properties.getDescription())
                            .version(properties.getVersion())
                            .build())
                    .select()
                    .apis(SpringFoxConfig.withMethodAnnotation())
                    .paths(PathSelectors.any())
                    .build()
                    .groupName(groupOpenApi.getDisplayName())
                    .securitySchemes(securitySchemes)
                    .securityContexts(securityContexts);
            if (i == 0) {
                defaultDocket = docket;
                continue;
            }
            beanFactory.registerSingleton(MessageFormat.format("{0}-Docket", groupOpenApi.getDisplayName()), docket);
        }
        return defaultDocket;
    }

    /**
     * 获取 Swagger API 接口信息
     * 兼容 Swagger 2.0、Swagger 3.0 注解写法
     *
     * @return 获取 Swagger API 接口信息
     */
    private static Predicate<RequestHandler> withMethodAnnotation() {
        return input -> input.isAnnotatedWith(ApiOperation.class) || input.isAnnotatedWith(Operation.class);
    }

}
