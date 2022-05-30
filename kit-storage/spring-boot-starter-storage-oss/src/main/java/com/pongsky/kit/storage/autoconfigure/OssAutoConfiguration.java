package com.pongsky.kit.storage.autoconfigure;

import com.aliyun.oss.OSS;
import com.pongsky.kit.storage.properties.OssProperties;
import com.pongsky.kit.storage.properties.StorageProperties;
import com.pongsky.kit.storage.utils.AliYunOssUtils;
import com.pongsky.kit.storage.utils.StorageUtils;
import com.pongsky.kit.storage.web.aspect.around.StorageAspect;
import com.pongsky.kit.storage.web.aspect.before.UploadAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 阿里云 OSS 自动装配
 *
 * @author pengsenhao
 */
@ConditionalOnClass({OSS.class})
@Configuration(proxyBeanMethods = false)
@Import({StorageAspect.class, UploadAspect.class})
@EnableConfigurationProperties({StorageProperties.class, OssProperties.class})
public class OssAutoConfiguration {

    /**
     * 获取阿里云 OSS 工具类
     *
     * @param properties properties
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    @Bean
    public StorageUtils storageUtils(OssProperties properties) {
        return new AliYunOssUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKeyId(), properties.getSecretAccessKey());
    }

}
