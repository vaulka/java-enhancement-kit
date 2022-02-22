package com.pongsky.kit.storage.config;

import com.pongsky.kit.utils.storage.AliYunOssUtils;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云 OSS 配置
 *
 * @author pengsenhao
 */
@Setter
@ConfigurationProperties("aliyun.oss")
public class AliYunOssConfig {

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
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 获取阿里云 OSS 工具类
     *
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    public AliYunOssUtils getUtils() {
        return new AliYunOssUtils(endpoint, bucket, accessKeyId, accessKeySecret);
    }

}
