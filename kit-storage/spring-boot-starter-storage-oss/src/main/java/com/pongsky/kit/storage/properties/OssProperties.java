package com.pongsky.kit.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云 OSS 配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties("aliyun.oss")
public class OssProperties {

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * bucket
     */
    private String bucket;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * secretAccessKey
     */
    private String secretAccessKey;

}
