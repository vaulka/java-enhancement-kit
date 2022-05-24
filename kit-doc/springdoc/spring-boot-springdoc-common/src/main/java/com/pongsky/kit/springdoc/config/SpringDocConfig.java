package com.pongsky.kit.springdoc.config;

import com.pongsky.kit.springdoc.properties.SpringDocProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SpringDoc Swagger 配置
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@ConditionalOnProperty(value = "doc.enabled", havingValue = "true", matchIfMissing = true)
public class SpringDocConfig {

    private final SpringDocProperties properties;
    private final ApplicationContext applicationContext;

    /**
     * 创建 Swagger 文档信息
     *
     * @return 创建 Swagger 文档信息
     */
    @Bean
    public OpenAPI defaultOpenApi() {
        Map<String, SecurityScheme> securitySchemes = properties.getRequestParameters().entrySet().stream()
                .map(e -> e.getValue().stream()
                        .map(v -> new SecurityScheme()
                                .name(v)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(e.getKey()))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(SecurityScheme::getName, v -> v));
        SecurityRequirement securityRequirement = new SecurityRequirement();
        properties.getRequestParameters().values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()).forEach(securityRequirement::addList);
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .description(properties.getDescription())
                        .version(properties.getVersion()))
                .components(new Components().securitySchemes(securitySchemes))
                .addSecurityItem(securityRequirement);
    }

    /**
     * 默认分组
     */
    private static final SpringDocProperties.GroupOpenApi DEFAULT_GROUPED_OPEN_API = new SpringDocProperties.GroupOpenApi()
            .setDisplayName("default")
            .setGroup("");

    /**
     * 创建 Swagger 组别信息
     *
     * @return 创建 Swagger 组别信息
     */
    @Bean
    public GroupedOpenApi defaultGroupedOpenApi() {
        if (properties.getGroups().size() == 0) {
            // 未填写组别则会有一个默认分组
            // 兼容 Knife4j 源码，没有分组则会报错
            return DEFAULT_GROUPED_OPEN_API.build();
        }
        List<String> displayNames = properties.getGroups().stream()
                .map(SpringDocProperties.GroupOpenApi::getDisplayName)
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
                .map(SpringDocProperties.GroupOpenApi::getGroup)
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
        GroupedOpenApi defaultGroupedOpenApi = null;
        for (int i = 0; i < properties.getGroups().size(); i++) {
            GroupedOpenApi groupedOpenApi = properties.getGroups().get(i).build();
            if (i == 0) {
                defaultGroupedOpenApi = groupedOpenApi;
                continue;
            }
            beanFactory.registerSingleton(MessageFormat.format("{0}-GroupedOpenAPI", groupedOpenApi.getDisplayName()), groupedOpenApi);
        }
        return defaultGroupedOpenApi;
    }

}
