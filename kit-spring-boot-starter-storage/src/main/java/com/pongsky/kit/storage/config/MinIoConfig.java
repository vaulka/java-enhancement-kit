package com.pongsky.kit.storage.config;

import com.pongsky.kit.utils.storage.MinIoUtils;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO 配置
 *
 * @author pengsenhao
 */
@Setter
@ConfigurationProperties("minio")
public class MinIoConfig {

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

    /**
     * 获取 MinIO 工具类
     *
     * @return 获取 MinIO 工具类
     * @author pengsenhao
     */
    public MinIoUtils getUtils() {
        return new MinIoUtils(endpoint, bucket, accessKey, secretKey);
    }

}
