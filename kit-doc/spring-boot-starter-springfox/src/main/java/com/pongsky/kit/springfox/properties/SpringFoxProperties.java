package com.pongsky.kit.springfox.properties;

import lombok.Getter;
import lombok.Setter;
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
    private Map<AuthIn, List<String>> requestParameter = Collections.emptyMap();

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

}
