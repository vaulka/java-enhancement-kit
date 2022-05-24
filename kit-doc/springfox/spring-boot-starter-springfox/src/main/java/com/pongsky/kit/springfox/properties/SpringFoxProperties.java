package com.pongsky.kit.springfox.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SpringFox 文档配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "doc")
public class SpringFoxProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

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
    private Map<AuthIn, List<String>> requestParameters = Collections.emptyMap();

    /**
     * 鉴权参数存放位置
     */
    public enum AuthIn {

        /**
         * header
         */
        header,

        /**
         * query
         */
        query,

        /**
         * cookie
         */
        cookie

    }

    /**
     * 组别列表
     * <p>
     * 未填写则会有一个默认分组
     */
    private List<GroupOpenApi> groups = Collections.emptyList();

    @Getter
    @Setter
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    public static class GroupOpenApi {

        /**
         * The Display name.
         */
        private String displayName;

        /**
         * The Group.
         */
        private String group;

    }

}
