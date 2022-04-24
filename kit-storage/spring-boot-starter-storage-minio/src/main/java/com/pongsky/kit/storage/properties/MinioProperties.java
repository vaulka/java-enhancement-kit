package com.pongsky.kit.storage.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * bucket
     */
    private String bucket;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

}
