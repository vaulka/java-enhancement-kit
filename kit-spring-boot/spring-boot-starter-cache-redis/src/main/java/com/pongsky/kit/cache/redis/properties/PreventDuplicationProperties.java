package com.pongsky.kit.cache.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 防重检测配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.prevent-duplication")
public class PreventDuplicationProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

}
