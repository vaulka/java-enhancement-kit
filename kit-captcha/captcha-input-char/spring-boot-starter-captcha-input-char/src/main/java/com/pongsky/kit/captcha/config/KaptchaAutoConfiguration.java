package com.pongsky.kit.captcha.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * 输入型字符 验证码 自动装配
 *
 * @author pengsenhao
 */
@Configuration(proxyBeanMethods = false)
@Import({KaptchaConfiguration.class})
@EnableConfigurationProperties({KaptchaProperties.class})
public class KaptchaAutoConfiguration {


}
