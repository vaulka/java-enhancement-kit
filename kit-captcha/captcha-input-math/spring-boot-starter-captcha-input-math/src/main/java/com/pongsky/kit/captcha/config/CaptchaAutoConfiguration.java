package com.pongsky.kit.captcha.config;

import com.pongsky.kit.captcha.properties.CaptchaProperties;
import com.pongsky.kit.captcha.utils.CaptchaUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 输入型算数 验证码 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({CaptchaProperties.class})
public class CaptchaAutoConfiguration {

    /**
     * 创建验证码工具类
     *
     * @param properties 验证码参数配置
     * @return 验证码工具类
     */
    @Bean
    public CaptchaUtils captchaUtils(CaptchaProperties properties) {
        return new CaptchaUtils(properties.getMinCode(), properties.getMaxCode(),
                (String.valueOf(properties.getMaxCode()).length() + 3)
                        * properties.getCodeWidthSpace(),
                properties.getImageHeight(),
                properties.getDrawCount(), properties.getLineWidth());
    }

}
