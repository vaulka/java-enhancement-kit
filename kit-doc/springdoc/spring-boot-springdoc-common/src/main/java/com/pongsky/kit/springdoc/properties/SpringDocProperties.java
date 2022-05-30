package com.pongsky.kit.springdoc.properties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springdoc.core.Constants.GROUP_NAME_NOT_NULL;

/**
 * SpringDoc 文档配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "doc")
public class SpringDocProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * Swagger 网关路由定位器 是否启用
     */
    private boolean gatewayRouteEnabled = false;

    /**
     * 文档标题
     */
    private String title = "API Docs";

    /**
     * 文档描述
     */
    private String description;

    /**
     * 文档版本号
     */
    private String version;

    /**
     * 请求参数信息
     * <p>
     * key: 鉴权参数存放位置
     * value：参数名称列表
     */
    private Map<SecurityScheme.In, List<String>> requestParameters = Collections.emptyMap();

    /**
     * 组别列表
     * <p>
     * 未填写则会有一个默认分组
     */
    @Valid
    private List<GroupOpenApi> groups = Collections.emptyList();

    /**
     * {@link org.springdoc.core.GroupedOpenApi}
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    public static class GroupOpenApi {

        /**
         * The Display name.
         */
        @NotBlank
        private String displayName;

        /**
         * The Group.
         */
        @NotNull
        private String group;

        /**
         * The Open api customisers.
         */
        private final List<OpenApiCustomiser> openApiCustomisers = new ArrayList<>();

        /**
         * The Operation customizers.
         */
        private final List<OperationCustomizer> operationCustomizers = new ArrayList<>();

        /**
         * The methods filters to apply.
         */
        private final List<OpenApiMethodFilter> methodFilters = new ArrayList<>();

        /**
         * The Paths to match.
         */
        private List<String> pathsToMatch = new ArrayList<>();

        /**
         * The Packages to scan.
         */
        private List<String> packagesToScan = new ArrayList<>();

        /**
         * The Packages to exclude.
         */
        private List<String> packagesToExclude = new ArrayList<>();

        /**
         * The Paths to exclude.
         */
        private List<String> pathsToExclude = new ArrayList<>();

        /**
         * The Produces to match.
         */
        private List<String> producesToMatch = new ArrayList<>();

        /**
         * The Headers to match.
         */
        private List<String> headersToMatch = new ArrayList<>();

        /**
         * The Consumes to match.
         */
        private List<String> consumesToMatch = new ArrayList<>();

        /**
         * Add method filter.
         *
         * @param methodFilter an additional filter to apply to the matched methods
         * @return the builder
         */
        public GroupOpenApi addOpenApiMethodFilter(OpenApiMethodFilter methodFilter) {
            this.methodFilters.add(methodFilter);
            return this;
        }

        /**
         * GroupOpenApi To GroupedOpenApi
         *
         * @return GroupedOpenApi
         */
        public GroupedOpenApi build() {
            this.addOpenApiMethodFilter(method -> method.isAnnotationPresent(Operation.class));
            GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
                    .group(Objects.requireNonNull(this.group, GROUP_NAME_NOT_NULL))
                    .pathsToMatch(this.pathsToMatch.toArray(new String[0]))
                    .packagesToScan(this.packagesToScan.toArray(new String[0]))
                    .producesToMatch(this.producesToMatch.toArray(new String[0]))
                    .consumesToMatch(this.consumesToMatch.toArray(new String[0]))
                    .headersToMatch(this.headersToMatch.toArray(new String[0]))
                    .packagesToExclude(this.packagesToExclude.toArray(new String[0]))
                    .pathsToExclude(this.pathsToExclude.toArray(new String[0]))
                    .displayName(StringUtils.defaultIfEmpty(this.displayName, this.group));
            for (OpenApiCustomiser openApiCustomiser : this.openApiCustomisers) {
                builder.addOpenApiCustomiser(openApiCustomiser);
            }
            for (OperationCustomizer operationCustomizer : this.operationCustomizers) {
                builder.addOperationCustomizer(operationCustomizer);
            }
            for (OpenApiMethodFilter openApiMethodFilter : this.methodFilters) {
                builder.addOpenApiMethodFilter(openApiMethodFilter);
            }
            return builder.build();
        }

    }

}
