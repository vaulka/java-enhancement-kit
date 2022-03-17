package com.pongsky.kit.captcha.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

/**
 * 输入型字符 验证码工具类
 *
 * @author pengsenhao
 */
public class KaptchaUtils {

    DefaultKaptcha defaultKaptcha;

    public KaptchaUtils(Properties properties) {
        defaultKaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
    }

    /**
     * 创建验证码 图像类型
     *
     * @param code 验证码
     * @return 创建验证码 图像类型
     */
    public BufferedImage createByImage(String code) {
        return defaultKaptcha.createImage(code);
    }

    /**
     * 图片格式类型
     */
    private static final String FORMAT_NAME = "jpg";

    /**
     * 创建验证码 OutputStream 流
     *
     * @param code 验证码
     * @return 创建验证码 OutputStream 流
     */
    public ByteArrayOutputStream createByOutputStream(String code) throws IOException {
        BufferedImage image = defaultKaptcha.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (outputStream) {
            ImageIO.write(image, FORMAT_NAME, outputStream);
        }
        return outputStream;
    }

    /**
     * 创建验证码 Base64 信息
     *
     * @param code 验证码
     * @return 创建验证码 Base64 信息
     */
    public String createByBase64(String code) throws IOException {
        BufferedImage image = defaultKaptcha.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (outputStream) {
            ImageIO.write(image, FORMAT_NAME, outputStream);
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
