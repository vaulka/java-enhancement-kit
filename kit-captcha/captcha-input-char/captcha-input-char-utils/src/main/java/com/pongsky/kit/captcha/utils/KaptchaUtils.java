package com.pongsky.kit.captcha.utils;

import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 输入型字符 验证码工具类
 *
 * @author pengsenhao
 */
public class KaptchaUtils {

    /**
     * 验证码数量
     */
    private final int codeNum;

    /**
     * 图像宽度
     */
    private final int imageWidth;

    /**
     * 图像高度
     */
    private final int imageHeight;

    /**
     * 干扰线数量
     */
    private final int drawCount;

    /**
     * 干扰线的长度 = 1.414 * LINE_WIDTH
     */
    private final int lineWidth;

    public KaptchaUtils(int codeNum,
                        int imageWidth, int imageHeight,
                        int drawCount, int lineWidth) {
        this.codeNum = codeNum;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.drawCount = drawCount;
        this.lineWidth = lineWidth;
    }

    /**
     * 创建图像验证码
     *
     * @param code 验证码
     * @return 创建图像验证码
     */
    private BufferedImage createImage(String code) {
        // 在内存中创建图像
        final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        // 设定背景颜色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.drawRect(0, 0, imageWidth - 1, imageHeight - 1);
        final Random random = new Random();
        // 随机产生干扰线，使图像中的认证码不易被其它程序探测到
        for (int i = 0; i < drawCount; i++) {
            graphics.setColor(getRandColor(150, 200));
            // 保证画在边框之内
            final int x = random.nextInt(imageWidth - lineWidth - 1) + 1;
            final int y = random.nextInt(imageHeight - lineWidth - 1) + 1;
            final int xl = random.nextInt(lineWidth);
            final int yl = random.nextInt(lineWidth);
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
     * 创建验证码
     *
     * @return 创建验证码
     */
    public String createCode() {
        return RandomStringUtils.randomAlphanumeric(codeNum);
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
     * @throws IOException IOException
     */
    public ByteArrayOutputStream createImageByStream(String code) throws IOException {
        BufferedImage image = this.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, FORMAT_NAME, outputStream);
        } finally {
            outputStream.close();
        }
        return outputStream;
    }

    /**
     * 创建图像验证码，并写入 Stream 流
     *
     * @param code         验证码
     * @param outputStream outputStream
     * @throws IOException IOException
     */
    public void createImageByStream(String code, OutputStream outputStream) throws IOException {
        BufferedImage image = this.createImage(code);
        try {
            ImageIO.write(image, FORMAT_NAME, outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 创建图像验证码 Base64 信息
     *
     * @param code 验证码
     * @return 创建验证码 Base64 信息
     * @throws IOException IOException
     */
    public String createImageByBase64(String code) throws IOException {
        ByteArrayOutputStream outputStream = this.createImageByStream(code);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
