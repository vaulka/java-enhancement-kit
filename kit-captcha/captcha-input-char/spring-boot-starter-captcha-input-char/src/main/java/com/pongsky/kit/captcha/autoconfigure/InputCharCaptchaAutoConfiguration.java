package com.pongsky.kit.captcha.autoconfigure;

import com.pongsky.kit.captcha.properties.InputCharCaptchaProperties;
import com.pongsky.kit.captcha.utils.InputCharCaptchaUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 输入型字符 验证码 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({InputCharCaptchaProperties.class})
public class InputCharCaptchaAutoConfiguration {

    /**
     * 创建验证码工具类
     *
     * @param properties 验证码参数配置
     * @return 验证码工具类
     */
    @Bean
    public InputCharCaptchaUtils captchaUtils(InputCharCaptchaProperties properties) {
        return new InputCharCaptchaUtils(properties.getCodeNum(),
                properties.getCodeNum() * properties.getCodeWidthSpace(),
                properties.getImageHeight(),
                properties.getDrawCount(), properties.getLineWidth());
    }

}
