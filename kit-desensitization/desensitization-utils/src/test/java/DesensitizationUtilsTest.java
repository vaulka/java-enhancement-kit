import com.pongsky.kit.desensitization.handler.BankCardDesensitizationHandler;
import com.pongsky.kit.desensitization.handler.EmailDesensitizationHandler;
import com.pongsky.kit.desensitization.handler.IdCardDesensitizationHandler;
import com.pongsky.kit.desensitization.handler.NameDesensitizationHandler;
import com.pongsky.kit.desensitization.handler.PasswordDesensitizationHandler;
import com.pongsky.kit.desensitization.handler.PhoneDesensitizationHandler;

/**
 * @author pengsenhao
 */
public class DesensitizationUtilsTest {

    public static void main(String[] args) {
        // 银行卡脱敏
        String bankCard = "6217681830027807807";
        System.out.println("bankCard desensitization：" + new BankCardDesensitizationHandler().exec(bankCard));
        System.out.println();

        // 邮箱脱敏
        String email = "kelry@vip.qq.com";
        System.out.println("email desensitization：" + new EmailDesensitizationHandler().exec(email));
        System.out.println();

        // 身份证脱敏
        String idCard = "371257199507072277";
        System.out.println("idCard desensitization:" + new IdCardDesensitizationHandler().exec(idCard));
        System.out.println();

        // 姓名脱敏
        String name = "彭森豪";
        System.out.println("name desensitization:" + new NameDesensitizationHandler().exec(name));
        System.out.println();

        // 密码脱敏
        String password = "123456";
        System.out.println("password desensitization:" + new PasswordDesensitizationHandler().exec(password));
        System.out.println();

        // 手机号脱敏
        String phone = "15753678879";
        System.out.println("phone desensitization:" + new PhoneDesensitizationHandler().exec(phone));

        System.out.println();
    }

}
