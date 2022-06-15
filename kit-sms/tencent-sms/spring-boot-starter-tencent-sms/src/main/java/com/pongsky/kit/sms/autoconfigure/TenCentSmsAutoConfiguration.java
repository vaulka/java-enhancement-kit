package com.pongsky.kit.sms.autoconfigure;

import com.pongsky.kit.sms.fail.TenCentSmsExceptionFailProcessor;
import com.pongsky.kit.sms.properties.TenCentSmsProperties;
import com.pongsky.kit.sms.utils.TenCentSmsUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 腾讯云 SMS 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({
        TenCentSmsExceptionFailProcessor.class
})
@EnableConfigurationProperties({TenCentSmsProperties.class})
public class TenCentSmsAutoConfiguration {

    /**
     * 获取腾讯云 SMS 工具类
     *
     * @param properties properties
     * @return 获取腾讯云 SMS 工具类
     */
    @ConditionalOnProperty(value = "tencent.sms.enabled", havingValue = "true", matchIfMissing = true)
    @Bean
    public TenCentSmsUtils tenCentSmsUtils(TenCentSmsProperties properties) {
        return new TenCentSmsUtils(properties.getRegion(), properties.getSdkAppId(),
                properties.getSecretId(), properties.getSecretKey());
    }

}
