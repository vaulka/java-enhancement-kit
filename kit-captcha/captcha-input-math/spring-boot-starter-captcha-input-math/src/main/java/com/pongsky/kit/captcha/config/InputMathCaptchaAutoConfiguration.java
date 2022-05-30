package com.pongsky.kit.captcha.config;

import com.pongsky.kit.captcha.properties.InputMathCaptchaProperties;
import com.pongsky.kit.captcha.utils.InputMathCaptchaUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 输入型算数 验证码 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({InputMathCaptchaProperties.class})
public class InputMathCaptchaAutoConfiguration {

    /**
     * 创建验证码工具类
     *
     * @param properties 验证码参数配置
     * @return 验证码工具类
     */
    @Bean
    public InputMathCaptchaUtils captchaUtils(InputMathCaptchaProperties properties) {
        return new InputMathCaptchaUtils(properties.getMinCode(), properties.getMaxCode(),
                (String.valueOf(properties.getMaxCode()).length() + 3)
                        * properties.getCodeWidthSpace(),
                properties.getImageHeight(),
                properties.getDrawCount(), properties.getLineWidth());
    }

}
