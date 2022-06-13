package com.pongsky.kit.storage.autoconfigure;

import com.pongsky.kit.storage.processor.fail.AliyunOssClientExceptionFailProcessor;
import com.pongsky.kit.storage.processor.fail.AliyunOssExceptionFailProcessor;
import com.pongsky.kit.storage.properties.AliyunOssProperties;
import com.pongsky.kit.storage.properties.StorageProperties;
import com.pongsky.kit.storage.utils.AliYunOssUtils;
import com.pongsky.kit.storage.web.aspect.before.UploadAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 阿里云 OSS 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({
        UploadAspect.class,
        AliyunOssClientExceptionFailProcessor.class, AliyunOssExceptionFailProcessor.class
})
@EnableConfigurationProperties({StorageProperties.class, AliyunOssProperties.class})
public class AliyunOssAutoConfiguration {

    /**
     * 获取阿里云 OSS 工具类
     *
     * @param properties properties
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    @ConditionalOnProperty(value = "aliyun.oss.enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public AliYunOssUtils aliYunOssUtils(AliyunOssProperties properties) {
        return new AliYunOssUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

}
