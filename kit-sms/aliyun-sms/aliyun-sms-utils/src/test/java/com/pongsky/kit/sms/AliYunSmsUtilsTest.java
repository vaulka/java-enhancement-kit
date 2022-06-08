package com.pongsky.kit.sms;

import com.pongsky.kit.sms.entity.SmsTemplate;
import com.pongsky.kit.sms.utils.AliYunSmsUtils;

/**
 * 阿里云 SMS 工具类
 *
 * @author pengsenhao
 */
public class AliYunSmsUtilsTest {

    public static class TestTemplate extends SmsTemplate<TestTemplateParam> {

        public TestTemplate(TestTemplateParam param) {
            super(param);
        }

        @Override
        public String getCode() {
            return "SMS_154950909";
        }

        @Override
        public String format() {
            return "您正在使用阿里云短信测试服务，体验验证码是：${code}，如非本人操作，请忽略本短信！";
        }

    }

    public static class TestTemplateParam {

        public TestTemplateParam(String code) {
            this.code = code;
        }

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * regionId
     */
    private static final String regionId = "cn-shanghai";

    /**
     * accessKeyId
     */
    private static final String accessKeyId = "";

    /**
     * accessKeySecret
     */
    private static final String accessKeySecret = "";

    private static final AliYunSmsUtils UTILS = new AliYunSmsUtils(regionId, accessKeyId, accessKeySecret);

    public static void main(String[] args) {
        UTILS.sendSms("阿里云短信测试", new TestTemplate(new TestTemplateParam("123456")), "1515****510");
    }

}
