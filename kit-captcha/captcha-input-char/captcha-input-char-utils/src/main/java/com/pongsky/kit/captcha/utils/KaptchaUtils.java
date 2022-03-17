package com.pongsky.kit.captcha.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 输入型字符 验证码工具类
 *
 * @author pengsenhao
 */
public class KaptchaUtils {

    /**
     * 最大颜色数
     */
    private static final int MAX_COLOR_NUM = 255;

    /**
     * 取得给定范围随机颜色
     *
     * @param fc fc
     * @param bc bc
     * @return 颜色
     */
    private Color getRandColor(int fc, int bc) {
        final Random random = new Random();
        if (fc > MAX_COLOR_NUM) {
            fc = MAX_COLOR_NUM;
        }
        if (bc > MAX_COLOR_NUM) {
            bc = MAX_COLOR_NUM;
        }
        final int r = fc + random.nextInt(bc - fc);
        final int g = fc + random.nextInt(bc - fc);
        final int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 定义图形大小
     */
    private static final int IMAGE_WIDTH = 105;
    /**
     * 定义图形大小
     */
    private static final int IMAGE_HEIGHT = 35;

    /**
     * 定义干扰线数量
     */
    private static final int DRAW_COUNT = 200;

    /**
     * 干扰线的长度 = 1.414 * LINE_WIDTH
     */
    private static final int LINE_WIDTH = 2;

    /**
     * 创建图像验证码
     *
     * @param code 验证码
     * @return 创建图像验证码
     */
    private BufferedImage createImage(String code) {
        // 在内存中创建图像
        final BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        // 设定背景颜色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        graphics.drawRect(0, 0, IMAGE_WIDTH - 1, IMAGE_HEIGHT - 1);
        final Random random = new Random();
        // 随机产生干扰线，使图像中的认证码不易被其它程序探测到
        for (int i = 0; i < DRAW_COUNT; i++) {
            graphics.setColor(getRandColor(150, 200));
            // 保证画在边框之内
            final int x = random.nextInt(IMAGE_WIDTH - LINE_WIDTH - 1) + 1;
            final int y = random.nextInt(IMAGE_HEIGHT - LINE_WIDTH - 1) + 1;
            final int xl = random.nextInt(LINE_WIDTH);
            final int yl = random.nextInt(LINE_WIDTH);
            graphics.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码
        for (int i = 0; i < code.length(); i++) {
            // 将认证码显示到图像中,调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            // 设置字体颜色
            graphics.setColor(Color.BLACK);
            // 设置字体样式
            graphics.setFont(new Font("Times New Roman", Font.BOLD, 24));
            // 设置字符，字符间距，上边距
            graphics.drawString(String.valueOf(code.charAt(i)), (23 * i) + 8, 26);
        }
        // 图像生效
        graphics.dispose();
        return image;
    }

    /**
     * 图片格式类型
     */
    private static final String FORMAT_NAME = "jpg";

    /**
     * 创建图像验证码 Stream 流
     *
     * @param code 验证码
     * @return 创建验证码 Stream 流
     */
    public ByteArrayOutputStream createImageByStream(String code) throws IOException {
        BufferedImage image = this.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (outputStream) {
            ImageIO.write(image, FORMAT_NAME, outputStream);
        }
        return outputStream;
    }

    /**
     * 创建图像验证码 Base64 信息
     *
     * @param code 验证码
     * @return 创建验证码 Base64 信息
     */
    public String createImageByBase64(String code) throws IOException {
        ByteArrayOutputStream outputStream = this.createImageByStream(code);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
