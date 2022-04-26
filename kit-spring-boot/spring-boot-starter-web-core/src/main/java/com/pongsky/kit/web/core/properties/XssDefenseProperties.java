package com.pongsky.kit.web.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * XSS 防御 参数配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(XssDefenseProperties.PROPERTIES_PREFIX)
public class XssDefenseProperties {

    /**
     * 参数配置 前缀
     */
    public static final String PROPERTIES_PREFIX = "xss-defense";

    /**
     * 是否启用 XSS 防御 KEY
     */
    public static final String ENABLED = "enabled";

    /**
     * 是否启用 XSS 防御 完整属性
     */
    public static final String ENABLED_PROPERTIES = PROPERTIES_PREFIX + "." + ENABLED;

    /**
     * 是否启用 XSS 防御
     */
    private boolean enabled = true;

    /**
     * URL 匹配规则列表
     */
    private List<String> urlPatterns = Collections.singletonList("/*");

    /**
     * 排除列表
     */
    private List<String> excludes = Collections.emptyList();

}
