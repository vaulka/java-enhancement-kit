import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.desensitization.annotation.DesensitizationMark;
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

    public static class User {

        /**
         * 银行卡脱敏
         */
        @DesensitizationMark(handler = BankCardDesensitizationHandler.class)
        private String bankCard = "6217681830027807807";

        /**
         * 邮箱脱敏
         */
        @DesensitizationMark(handler = EmailDesensitizationHandler.class)
        private String email = "kelry@vip.qq.com";

        /**
         * 身份证脱敏
         */
        @DesensitizationMark(handler = IdCardDesensitizationHandler.class)
        private String idCard = "371257199507072277";

        /**
         * 姓名脱敏
         */
        @DesensitizationMark(handler = NameDesensitizationHandler.class)
        private String name = "彭森豪";

        /**
         * 密码脱敏
         */
        @DesensitizationMark(handler = PasswordDesensitizationHandler.class)
        private String password = "123456";

        /**
         * 手机号脱敏
         */
        @DesensitizationMark(handler = PhoneDesensitizationHandler.class)
        private String phone = "15753678879";

        public String getBankCard() {
            return bankCard;
        }

        public String getEmail() {
            return email;
        }

        public String getIdCard() {
            return idCard;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public String getPhone() {
            return phone;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(MAPPER.writeValueAsString(new User()));
        System.out.println(MAPPER.writeValueAsString(new User()));
    }

}
