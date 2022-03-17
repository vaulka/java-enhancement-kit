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
     * @param kaptchaProperties 验证码参数配置
     * @return 验证码工具类
     */
    @Bean
    public KaptchaUtils kaptchaUtils(KaptchaProperties kaptchaProperties) {
        return new KaptchaUtils(kaptchaProperties.getCodeNum(),
                kaptchaProperties.getCodeNum() * kaptchaProperties.getCodeWidthSpace(),
                kaptchaProperties.getImageHeight(),
                kaptchaProperties.getDrawCount(), kaptchaProperties.getLineWidth());
    }

}
