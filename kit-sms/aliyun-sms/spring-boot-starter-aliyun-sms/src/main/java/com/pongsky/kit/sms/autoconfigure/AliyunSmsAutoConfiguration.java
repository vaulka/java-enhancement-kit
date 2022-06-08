package com.pongsky.kit.sms.autoconfigure;

import com.pongsky.kit.sms.fail.AliyunOssClientExceptionFailProcessor;
import com.pongsky.kit.sms.fail.AliyunSmsBizExceptionFailProcessor;
import com.pongsky.kit.sms.properties.AliyunSmsProperties;
import com.pongsky.kit.sms.utils.AliYunSmsUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 阿里云 SMS 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({
        AliyunOssClientExceptionFailProcessor.class, AliyunSmsBizExceptionFailProcessor.class
})
@EnableConfigurationProperties({AliyunSmsProperties.class})
public class AliyunSmsAutoConfiguration {

    /**
     * 获取阿里云 SMS 工具类
     *
     * @param properties properties
     * @return 获取阿里云 SMS 工具类
     */
    @Bean
    public AliYunSmsUtils aliYunSmsUtils(AliyunSmsProperties properties) {
        return new AliYunSmsUtils(properties.getRegionId(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

}
