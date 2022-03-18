package com.pongsky.kit.captcha.config;

import com.pongsky.kit.captcha.utils.KaptchaUtils;
import org.springframework.context.annotation.Bean;


/**
 * 输入型算数 验证码 配置
 *
 * @author pengsenhao
 */
public class KaptchaConfiguration {

    /**
     * 创建验证码工具类
     *
     * @param properties 验证码参数配置
     * @return 验证码工具类
     */
    @Bean
    public KaptchaUtils kaptchaUtils(KaptchaProperties properties) {
        return new KaptchaUtils(properties.getMinCode(), properties.getMaxCode(),
                (String.valueOf(properties.getMaxCode()).length() + 3)
                        * properties.getCodeWidthSpace(),
                properties.getImageHeight(),
                properties.getDrawCount(), properties.getLineWidth());
    }

}
