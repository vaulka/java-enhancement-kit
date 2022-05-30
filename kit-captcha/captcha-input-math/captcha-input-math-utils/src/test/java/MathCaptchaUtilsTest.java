import com.pongsky.kit.captcha.utils.InputMathCaptchaUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author pengsenhao
 */
public class MathCaptchaUtilsTest {

    public static void main(String[] args) throws IOException {
        // 验证码最小值
        int minCode = 10;

        // 验证码最大值
        int maxCode = 99;

        // 创建输入型算数 验证码工具类
        InputMathCaptchaUtils captchaUtils = new InputMathCaptchaUtils(10, 99,
                (String.valueOf(maxCode).length() + 3) * 25,
                35, 200, 2);

        // 生成验证码
        // 也可自己定义验证码
        int code = captchaUtils.createCode();

        // 根据验证码生成图片，将该图片转成 Base64 字符串
        String imageByBase64 = captchaUtils.createImageByBase64(code);

        code = RandomUtils.nextInt(minCode, maxCode);

        // 根据验证码生成图片，将该图片转成 ByteArrayOutputStream
        ByteArrayOutputStream imageByStream = captchaUtils.createImageByStream(code);

        code = RandomUtils.nextInt(minCode, maxCode);

        // 根据验证码生成图片，将该图片转成指定的 OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captchaUtils.createImageByStream(code, outputStream);

        System.out.println();
    }

}
