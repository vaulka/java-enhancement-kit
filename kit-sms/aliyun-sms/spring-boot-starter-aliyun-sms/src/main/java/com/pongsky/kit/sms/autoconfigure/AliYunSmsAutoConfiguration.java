package com.pongsky.kit.sms.autoconfigure;

import com.pongsky.kit.sms.fail.AliYunSmsBizExceptionFailProcessor;
import com.pongsky.kit.sms.fail.AliYunSmsClientExceptionFailProcessor;
import com.pongsky.kit.sms.properties.AliYunSmsProperties;
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
        AliYunSmsClientExceptionFailProcessor.class, AliYunSmsBizExceptionFailProcessor.class
})
@EnableConfigurationProperties({AliYunSmsProperties.class})
public class AliYunSmsAutoConfiguration {

    /**
     * 获取阿里云 SMS 工具类
     *
     * @param properties properties
     * @return 获取阿里云 SMS 工具类
     */
    @Bean
    public AliYunSmsUtils aliYunSmsUtils(AliYunSmsProperties properties) {
        return new AliYunSmsUtils(properties.getRegionId(),
                properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

}
