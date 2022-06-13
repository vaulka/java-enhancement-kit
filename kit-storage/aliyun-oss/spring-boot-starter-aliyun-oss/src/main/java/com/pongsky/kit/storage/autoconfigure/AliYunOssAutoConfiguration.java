package com.pongsky.kit.storage.autoconfigure;

import com.pongsky.kit.storage.processor.fail.AliYunOssClientExceptionFailProcessor;
import com.pongsky.kit.storage.processor.fail.AliYunOssExceptionFailProcessor;
import com.pongsky.kit.storage.properties.AliYunOssProperties;
import com.pongsky.kit.storage.properties.StorageProperties;
import com.pongsky.kit.storage.utils.AliYunOssUtils;
import com.pongsky.kit.storage.web.aspect.before.UploadAspect;
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
        AliYunOssClientExceptionFailProcessor.class, AliYunOssExceptionFailProcessor.class
})
@EnableConfigurationProperties({StorageProperties.class, AliYunOssProperties.class})
public class AliYunOssAutoConfiguration {

    /**
     * 获取阿里云 OSS 工具类
     *
     * @param properties properties
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    @Bean
    public AliYunOssUtils aliYunOssUtils(AliYunOssProperties properties) {
        return new AliYunOssUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

}
