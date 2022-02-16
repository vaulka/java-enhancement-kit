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
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-13 3:44 下午
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
     * @description 详细写出代码处理流程, 方法内也要详细注释
     * @author pengsenhao
     * @date 2022-02-16 7:54 上午
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
         * @description 详细写出代码处理流程, 方法内也要详细注释
         * @author pengsenhao
         * @date 2022-02-13 6:59 下午
         */
        public static AliYunOssUtils getUtils() {
            return new AliYunOssUtils(endpoint, bucket, accessKeyId, accessKeySecret);
        }

    }

}
