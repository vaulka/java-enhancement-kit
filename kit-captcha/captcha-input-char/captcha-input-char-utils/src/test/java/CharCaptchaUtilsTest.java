import com.pongsky.kit.captcha.utils.InputCharCaptchaUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author pengsenhao
 */
public class CharCaptchaUtilsTest {

    public static void main(String[] args) throws IOException {
        // 验证码个数
        int codeNum = 5;

        // 创建输入型字符 验证码工具类
        InputCharCaptchaUtils captchaUtils = new InputCharCaptchaUtils(codeNum, codeNum * 25, 35, 200, 2);

        // 创建验证码
        // 也可自己定义验证码
        String code = captchaUtils.createCode();

        // 根据验证码生成图片，将该图片转成 Base64 字符串
        String imageByBase64 = captchaUtils.createImageByBase64(code);

        code = RandomStringUtils.randomAlphanumeric(codeNum);

        // 根据验证码生成图片，将该图片转成 ByteArrayOutputStream
        ByteArrayOutputStream imageByStream = captchaUtils.createImageByStream(code);

        code = RandomStringUtils.randomAlphanumeric(codeNum);

        // 根据验证码生成图片，将该图片转成指定的 OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captchaUtils.createImageByStream(code, outputStream);

        System.out.println();
    }

}
