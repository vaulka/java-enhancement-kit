package com.pongsky.kit.storage.config;

import com.pongsky.kit.storage.utils.MinIoUtils;
import com.pongsky.kit.storage.utils.StorageUtils;
import com.pongsky.kit.storage.web.aspect.around.StorageAspect;
import com.pongsky.kit.storage.web.aspect.before.UploadAspect;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

/**
 * MinIO 自动装配
 *
 * @author pengsenhao
 */
@ConditionalOnClass({MinioClient.class})
@Configuration(proxyBeanMethods = false)
@Import({StorageAspect.class, UploadAspect.class})
@EnableConfigurationProperties({StorageProperties.class, MinioProperties.class})
public class MinioAutoConfiguration {

    /**
     * 获取 MinIO 工具类
     *
     * @param properties properties
     * @return 获取 MinIO 工具类
     * @author pengsenhao
     */
    @Bean
    public StorageUtils storageUtils(MinioProperties properties) {
        Assert.notNull(properties.getEndpoint(), "yml 配置 minio.endpoint 不能为空");
        Assert.notNull(properties.getBucket(), "yml 配置 minio.bucket 不能为空");
        Assert.notNull(properties.getAccessKey(), "yml 配置 minio.accessKey 不能为空");
        Assert.notNull(properties.getSecretKey(), "yml 配置 minio.secretKey 不能为空");
        return new MinIoUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKey(), properties.getSecretKey());
    }

}
