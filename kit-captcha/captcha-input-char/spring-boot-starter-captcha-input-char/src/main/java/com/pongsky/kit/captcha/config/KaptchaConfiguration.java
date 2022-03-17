package com.pongsky.kit.captcha.config;

import com.pongsky.kit.captcha.utils.KaptchaUtils;
import org.springframework.context.annotation.Bean;


/**
 * 文字验证码配置
 *
 * @author pengsenhao
 */
public class KaptchaConfiguration {

    /**
     * 创建验证码工具类
     *
     * @return 验证码工具类
     */
    @Bean
    public KaptchaUtils kaptchaUtils() {
        return new KaptchaUtils();
    }

}
