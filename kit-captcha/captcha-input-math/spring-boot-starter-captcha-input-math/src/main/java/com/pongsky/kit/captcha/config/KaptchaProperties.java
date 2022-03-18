package com.pongsky.kit.captcha.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 输入型算数 验证码 参数配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties("kaptcha")
public class KaptchaProperties {

    /**
     * 最小验证码数值
     */
    private int minCode = 10;

    /**
     * 最大验证码数值
     */
    private int maxCode = 99;

    /**
     * 每个验证码宽度间距
     */
    private int codeWidthSpace = 25;

    /**
     * 图像高度
     */
    private int imageHeight = 35;

    /**
     * 干扰线数量
     */
    private int drawCount = 200;

    /**
     * 干扰线的长度 = 1.414 * LINE_WIDTH
     */
    private int lineWidth = 2;

}
