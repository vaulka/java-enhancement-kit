package com.pongsky.springcloud.config;

import com.pongsky.springcloud.utils.storage.AliYunOssUtils;
import com.pongsky.springcloud.utils.storage.StorageType;
import com.pongsky.springcloud.utils.storage.StorageUtils;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储配置
 *
 * @author pengsenhao
 */
@Data
@Configuration
@ConfigurationProperties("storage")
public class StorageConfig {

    /**
     * 文件基地址
     */
    private String baseUri = "";

    /**
     * 云存储类型
     */
    private StorageType type = StorageType.none;

    /**
     * 获取云存储工具类
     *
     * @return 获取云存储工具类
     * @author pengsenhao
     */
    public StorageUtils getUtils() {
        switch (this.type) {
            case aliyun:
                return StorageConfig.AliYunOssConfig.getUtils();
            case minio:
            case none:
            default:
                return null;
        }
    }

    @Getter
    @Configuration
    @ConfigurationProperties("aliyun.oss")
    @ConditionalOnProperty(value = "storage.type", havingValue = "aliyun")
    public static class AliYunOssConfig {

        /**
         * endpoint
         */
        private static String endpoint;

        /**
         * bucket
         */
        private static String bucket;

        /**
         * accessKeyId
         */
        private static String accessKeyId;

        /**
         * accessKeySecret
         */
        private static String accessKeySecret;

        public void setEndpoint(String endpoint) {
            AliYunOssConfig.endpoint = endpoint;
        }

        public void setBucket(String bucket) {
            AliYunOssConfig.bucket = bucket;
        }

        public void setAccessKeyId(String accessKeyId) {
            AliYunOssConfig.accessKeyId = accessKeyId;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            AliYunOssConfig.accessKeySecret = accessKeySecret;
        }

        /**
         * 获取阿里云 OSS 工具类
         *
         * @return 获取阿里云 OSS 工具类
         * @author pengsenhao
         */
        public static AliYunOssUtils getUtils() {
            return new AliYunOssUtils(endpoint, bucket, accessKeyId, accessKeySecret);
        }

    }

}
