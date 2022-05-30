package com.pongsky.kit.storage.autoconfigure;

import com.pongsky.kit.storage.properties.MinioProperties;
import com.pongsky.kit.storage.properties.StorageProperties;
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
        return new MinIoUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKey(), properties.getSecretKey());
    }

}
