package com.pongsky.kit.storage.config;

import com.aliyun.oss.OSS;
import com.pongsky.kit.storage.utils.AliYunOssUtils;
import com.pongsky.kit.storage.utils.StorageUtils;
import com.pongsky.kit.storage.web.aspect.around.StorageAspect;
import com.pongsky.kit.storage.web.aspect.before.UploadAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2022-03-12 3:54 下午
 */
@ConditionalOnClass({OSS.class})
@Configuration(proxyBeanMethods = false)
@Import({StorageAspect.class, UploadAspect.class})
@EnableConfigurationProperties({StorageProperties.class, OssProperties.class})
public class OssAutoConfiguration {

    /**
     * 获取阿里云 OSS 工具类
     *
     * @return 获取阿里云 OSS 工具类
     * @author pengsenhao
     */
    @Bean
    public StorageUtils storageUtils(OssProperties properties) {
        Assert.notNull(properties.getEndpoint(), "yml 配置 aliyun.oss.endpoint 不能为空");
        Assert.notNull(properties.getBucket(), "yml 配置 aliyun.oss.bucket 不能为空");
        Assert.notNull(properties.getAccessKeyId(), "yml 配置 aliyun.oss.accessKeyId 不能为空");
        Assert.notNull(properties.getSecretAccessKey(), "yml 配置 aliyun.oss.secretAccessKey 不能为空");
        return new AliYunOssUtils(properties.getEndpoint(), properties.getBucket(),
                properties.getAccessKeyId(), properties.getSecretAccessKey());
    }

}
