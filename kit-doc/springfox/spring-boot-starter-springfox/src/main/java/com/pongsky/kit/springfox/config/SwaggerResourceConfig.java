package com.pongsky.kit.springfox.config;

import com.pongsky.kit.springfox.properties.SpringFoxProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger Resource 配置
 * <p>
 * 将 Swagger Resource 配置信息生成规则参照 SpringDoc 格式实现
 *
 * @author pengsenhao
 */
@Slf4j
@Primary
@AllArgsConstructor
@ConditionalOnProperty(value = "doc.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private final SpringFoxProperties properties;

    /**
     * 默认 Swagger API Docs URL
     */
    private static final String DEFAULT_SWAGGER_URL = "/v3/api-docs";

    /**
     * Swagger API Docs URL
     */
    private static final String SWAGGER_URL = "/v3/api-docs{0}";

    /**
     * Swagger Resource
     */
    private static final List<SwaggerResource> RESOURCES = new ArrayList<>();

    @Override
    public List<SwaggerResource> get() {
        if (RESOURCES.size() > 0) {
            return RESOURCES;
        }
        if (properties.getGroups().size() == 0) {
            RESOURCES.add(this.buildSwaggerResource(Docket.DEFAULT_GROUP_NAME, DEFAULT_SWAGGER_URL));
        } else {
            RESOURCES.addAll(properties.getGroups().stream()
                    .map(g -> this.buildSwaggerResource(g.getDisplayName(), MessageFormat.format(SWAGGER_URL, g.getGroup())))
                    .collect(Collectors.toList()));
        }
        return RESOURCES;
    }

    /**
     * 构建 Swagger Resource
     *
     * @param name 组别名称
     * @param url  资源地址
     * @return Swagger Resource
     */
    private SwaggerResource buildSwaggerResource(String name, String url) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setUrl(url);
        return swaggerResource;
    }

}