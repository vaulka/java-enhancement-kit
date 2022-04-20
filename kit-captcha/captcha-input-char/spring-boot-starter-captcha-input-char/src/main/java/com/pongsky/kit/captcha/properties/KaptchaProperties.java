package com.pongsky.kit.captcha.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 输入型字符 验证码 参数配置
 *
 * @author pengsenhao
 */
@Getter
@Setter
@ConfigurationProperties("kaptcha")
public class KaptchaProperties {

    /**
     * 验证码数量
     */
    private int codeNum = 5;

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
