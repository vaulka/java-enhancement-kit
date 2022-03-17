package com.pongsky.kit.captcha.config;

import com.pongsky.kit.captcha.utils.KaptchaUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Properties;


/**
 * 文字验证码配置
 *
 * @author pengsenhao
 */
public class KaptchaConfiguration {

    /**
     * 创建验证码工具类
     * <p>
     * 如需设置验证码参数，可自行创建该 bean
     *
     * @return 验证码工具类
     */
    @ConditionalOnMissingBean(KaptchaUtils.class)
    @Bean
    public KaptchaUtils kaptchaUtils() {
        Properties properties = new Properties();

        // 图片边框
        // 合法值：yes / no
        // 默认值：yes
        properties.setProperty("kaptcha.border", "yes");

        // 边框颜色
        // 合法值：r,g,b / white,black,blue.
        // 默认值：black
        properties.setProperty("kaptcha.border.color", "black");

        // 边框厚度
        // 合法值：> 0
        // 默认值：1
        properties.setProperty("kaptcha.border.thickness", "1");

        // 图片宽度
        // 默认值：200
        properties.setProperty("kaptcha.image.width", "200");

        // 图片高度
        // 默认值：50
        properties.setProperty("kaptcha.image.height", "50");

        // 图片实现类
        // 默认值：com.google.code.kaptcha.impl.DefaultKaptcha
        properties.setProperty("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha");

        // 文本实现类
        // 默认值：com.google.code.kaptcha.text.impl.DefaultTextCreator
        properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");

        // 文本集合，验证码值从此集合中获取
        // 默认值：abcde2345678gfynmnpwx
        properties.setProperty("kaptcha.textproducer.char.string", "abcde2345678gfynmnpwx");

        // 验证码长度
        // 默认值：5
        properties.setProperty("kaptcha.textproducer.char.length", "5");

        // 文字间隔
        // 默认值：2
        properties.setProperty("kaptcha.textproducer.char.space", "2");

        // 字体，多个,间隔
        // 默认值：Arial,Courier
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");

        // 字体大小，单位 px
        // 默认值：40
        properties.setProperty("kaptcha.textproducer.font.size", "40");

        // 字体颜色
        // 合法值：r,g,b / white,black,blue
        // 默认值：black
        properties.setProperty("kaptcha.textproducer.font.color", "black");

        // 干扰实现类
        // 默认值：com.google.code.kaptcha.impl.DefaultNoise
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");

        // 干扰颜色
        // 合法值：r,g,b / white,black,blue
        // 默认值：black
        properties.setProperty("kaptcha.noise.color", "black");

        // 图片样式
        // 合法值：
        // 水纹：com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼：com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影：com.google.code.kaptcha.impl.ShadowGimpy
        // 默认值：com.google.code.kaptcha.impl.WaterRipple
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");

        // 背景实现类
        // 默认值：com.google.code.kaptcha.impl.DefaultBackground
        properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");

        // 背景颜色渐变，开始颜色
        // 默认值：lightGray
        properties.setProperty("kaptcha.background.clear.from", "lightGray");

        // 背景颜色渐变，结束颜色
        // 默认值：white
        properties.setProperty("kaptcha.background.clear.to", "white");

        // 文字渲染器
        // 默认值：com.google.code.kaptcha.text.impl.DefaultWordRenderer
        properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");

        // session key
        // 默认值：KAPTCHA_SESSION_KEY
        properties.setProperty("kaptcha.session.key", "KAPTCHA_SESSION_KEY");

        // session date
        // 默认值：KAPTCHA_SESSION_DATE
        properties.setProperty("kaptcha.session.date", "KAPTCHA_SESSION_DATE");

        return new KaptchaUtils(properties);
    }

}
