package com.pongsky.kit.sms;

import com.pongsky.kit.sms.entity.SmsTemplate;
import com.pongsky.kit.sms.utils.TenCentSmsUtils;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

import java.util.Arrays;
import java.util.List;

/**
 * 腾讯云 SMS 工具类
 *
 * @author pengsenhao
 */
public class TenCentSmsUtilsTest {

    public static class TestTemplate extends SmsTemplate {

        public TestTemplate(List<String> param) {
            super(param);
        }

        @Override
        public String getId() {
            return "";
        }

        @Override
        public String format() {
            return "{1} 为您的登录验证码，请于 {2} 分钟内填写。如非本人操作，请忽略本短信。";
        }

    }

    /**
     * region
     */
    private static final String region = "ap-guangzhou";

    /**
     * sdkAppId
     */
    private static final String sdkAppId = "";

    /**
     * secretId
     */
    private static final String secretId = "";

    /**
     * secretKey
     */
    private static final String secretKey = "";

    private static final TenCentSmsUtils UTILS = new TenCentSmsUtils(region, sdkAppId, secretId, secretKey);

    public static void main(String[] args) {
        List<SendStatus> sendStatuses = UTILS.sendSms("小彭同学生活记录",
                new TestTemplate(Arrays.asList("13579", "5")),
                "1515***5510");
        System.out.println(sendStatuses.toString());
    }

}
