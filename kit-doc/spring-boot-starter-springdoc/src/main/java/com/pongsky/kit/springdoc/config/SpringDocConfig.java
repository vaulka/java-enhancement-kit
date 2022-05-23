package com.pongsky.kit.springdoc.config;

import com.pongsky.kit.springdoc.properties.SpringDocProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
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

    /**
     * 创建 Swagger 配置信息
     *
     * @return 创建 Swagger 配置信息
     */
    @Bean
    public OpenAPI defaultOpenApi() {
        Map<String, SecurityScheme> securitySchemes = properties.getRequestParameter().entrySet().stream()
                .map(e -> e.getValue().stream()
                        .map(v -> new SecurityScheme()
                                .name(v)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(e.getKey()))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(SecurityScheme::getName, v -> v));
        SecurityRequirement securityRequirement = new SecurityRequirement();
        properties.getRequestParameter().values().stream()
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

}
