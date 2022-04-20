package com.pongsky.kit.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 云存储配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * 文件基地址
     */
    private String baseUri = "";

}
