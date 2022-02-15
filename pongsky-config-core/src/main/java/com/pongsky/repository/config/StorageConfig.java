package com.pongsky.repository.config;

import com.pongsky.repository.utils.model.emums.Active;
import com.pongsky.repository.utils.storage.AliYunOssUtils;
import com.pongsky.repository.utils.storage.StorageType;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-02-13 3:44 下午
 */
@Configuration
@ConditionalOnProperty(value = "storage.enable", havingValue = "true")
public class StorageConfig {

    /**
     * 文件基地址
     */
    private static String baseUri;

    public static String getBaseUri() {
        return baseUri;
    }

    @Value("${storage.base-uri:}")
    public void setBaseUri(String baseUri) {
        StorageConfig.baseUri = baseUri;
    }

    /**
     * 云存储类型
     */
    private static StorageType type;

    public static StorageType getType() {
        return type;
    }

    @Value("${storage.type:none}")
    public void setType(String type) {
        StorageConfig.type = StorageType.valueOf(type);
    }

    @Setter
    @Configuration
    @ConfigurationProperties("aliyun.oss")
    @ConditionalOnProperty(value = "storage.type", havingValue = "aliyun")
    public static class AliYunOssConfig {

        /**
         * 外网 endpoint
         */
        private static String extranetEndpoint;

        /**
         * 内网 endpoint
         */
        private static String intranetEndpoint;

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

        public void setExtranetEndpoint(String extranetEndpoint) {
            AliYunOssConfig.extranetEndpoint = extranetEndpoint;
        }

        public void setIntranetEndpoint(String intranetEndpoint) {
            AliYunOssConfig.intranetEndpoint = intranetEndpoint;
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
         * 注册 阿里云 OSS
         *
         * @return 注册 阿里云 OSS
         * @description 详细写出代码处理流程, 方法内也要详细注释
         * @author pengsenhao
         * @date 2022-02-13 6:59 下午
         */
        public static AliYunOssUtils getUtils() {
            return new AliYunOssUtils(
                    Active.dev.name().equals(SystemConfig.getActive())
                            || Active.beta.name().equals(SystemConfig.getActive())
                            || Active.prod.name().equals(SystemConfig.getActive())
                            ? intranetEndpoint
                            : extranetEndpoint,
                    bucket, accessKeyId, accessKeySecret);
        }

    }

}
