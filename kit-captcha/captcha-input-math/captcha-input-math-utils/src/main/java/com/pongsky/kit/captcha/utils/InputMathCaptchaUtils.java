package com.pongsky.kit.captcha.utils;

import com.pongsky.kit.captcha.enums.MathType;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Random;

/**
 * 输入型算数 验证码工具类
 *
 * @author pengsenhao
 */
public class InputMathCaptchaUtils {

    /**
     * 最小验证码数值
     * <p>
     * 最小 10
     */
    private final int minCode;

    /**
     * 最大验证码数值
     */
    private final int maxCode;

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

    public InputMathCaptchaUtils(int minCode, int maxCode,
                                 int imageWidth, int imageHeight,
                                 int drawCount, int lineWidth) {
        this.minCode = minCode;
        this.maxCode = maxCode;
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
    private BufferedImage createImage(int code) {
        String codeText = this.createCode(code);
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
        int x = 0;
        for (int i = 0; i < codeText.length(); i++) {
            // 将认证码显示到图像中,调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            // 设置字体颜色
            graphics.setColor(Color.BLACK);
            // 设置字体样式
            graphics.setFont(new Font("Times New Roman", Font.BOLD, 24));
            // 设置字符，字符间距，上边距
            String str = String.valueOf(codeText.charAt(i));
            if (NumberUtils.isCreatable(str)) {
                // 数字与数字之间间隔小点
                x += 6 + 8;
            } else {
                // 遇到符号则间隔大点
                // FORMAT 中间有空格，所以这边的 x 轴增加较小
                x += 4 + 4;
            }
            graphics.drawString(str, x, 26);
        }
        // 图像生效
        graphics.dispose();
        return image;
    }

    /**
     * 验证码格式
     */
    private static final String CODE_FORMAT = "{0} {1} {2} =  ?";

    /**
     * 最大数值
     */
    private static final int MAX_NUM = 100;

    /**
     * 获取验证码文本
     *
     * @param code 验证码
     * @return 获取验证码文本
     */
    private String createCode(int code) {
        int num = RandomUtils.nextInt(1, 9);
        MathType mathType = MathType.ALL.get(new Random().nextInt(MathType.ALL.size()));
        switch (mathType) {
            case ADD:
                return MessageFormat.format(CODE_FORMAT,
                        code - num,
                        mathType.getCode(), num);
            case SUBTRACT:
                return MessageFormat.format(CODE_FORMAT,
                        code + num,
                        mathType.getCode(), num);
            case MULTIPLY:
                if (code % num > 0) {
                    num = code;
                }
                return MessageFormat.format(CODE_FORMAT,
                        code / num,
                        mathType.getCode(), num);
            case DIVIDE:
                if (code * num > MAX_NUM) {
                    // 数字太大的话，不好口算，用户体验会很差
                    num = 1;
                }
                return MessageFormat.format(CODE_FORMAT,
                        code * num,
                        mathType.getCode(), num);
            default:
                return "";
        }
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
    public int createCode() {
        return RandomUtils.nextInt(minCode, maxCode);
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
    public ByteArrayOutputStream createImageByStream(int code) throws IOException {
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
    public void createImageByStream(int code, OutputStream outputStream) throws IOException {
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
    public String createImageByBase64(int code) throws IOException {
        ByteArrayOutputStream outputStream = this.createImageByStream(code);
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

}
